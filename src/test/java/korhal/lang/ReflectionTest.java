package korhal.lang;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class Super {
  public void pub(String s) {
    System.out.println("pub: " + s);
  }

  private void priv(String s) {
    System.out.println("priv: " + s);
  }
}

class Sub extends Super {
}

public class ReflectionTest {
  @Test
  public void sub_call_sup_pub_test() {
    Sub sub = new Sub();
    sub.pub("Hello, this is sub");
    // sub.priv();          // not accessible: invisible to sub
  }

  /**
   * Sub class can access Super class's private method via java reflection
   * mechanism. Not suggested.
   *
   * m.setAccessible(true): disable Java's access checks, then
   * m.invoke("private", ...) works
   *
   * m.setAccessible(false): just re-enable access checks, so
   * m.invoke("public", ...) works no matter access checks is enabled or
   * disabled
   *
   * @throws Exception
   */
  @Test
  public void reflect_override_access_control_test() throws Exception {
    Sub sub = new Sub();
    Method m = Super.class.getDeclaredMethod("priv", String.class);
    m.setAccessible(true);
    m.invoke(sub, "Sub's reflection, priv: m.setAccessible(true).");
    Method m2 = Super.class.getDeclaredMethod("pub", String.class);
    m2.invoke(sub, "Sub's reflection, pub: no need to setAccessible(true)");
    m2.setAccessible(false);
    m2.invoke(sub, "Sub's reflection, pub: m.setAccessible(false)\n");
  }

  @Test
  public void Class_object_test() {
    // Class is derived from Object
    assertTrue(Object.class.isAssignableFrom(Class.class));

    // 5 ways to get Class object
    Super sup = new Super();
    Sub sub = new Sub();

    // a) .class
    assertTrue(Super.class.isAssignableFrom(Sub.class));
    assertTrue(Super.class.isAssignableFrom(Super.class));
    assertFalse(Sub.class.isAssignableFrom(Super.class));
    // b) Class.forName()
    try {
      Class<?> csub = Class.forName("korhal.lang.Sub");
      Class<?> csup = Class.forName("korhal.lang.Super");
      assertTrue(csup.isAssignableFrom(csub));
      assertTrue(csup.isAssignableFrom(Sub.class));
      assertFalse(csub.isAssignableFrom(Super.class));

      assertTrue(csub instanceof Class); // csub is a Class object
      assertTrue(csup instanceof Class); // csup is a Class object
                                         //
      assertTrue(sup instanceof Super);
      assertTrue(sub instanceof Sub);

      assertTrue(sub instanceof Super);
      assertFalse(sup instanceof Sub);

    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    // c) object.getClass()
    assertTrue(Super.class == sup.getClass());
    assertEquals(Super.class, sup.getClass());
    // d) Primitive Type class, differentiate int.class from Integer.class
    assertTrue(int.class == Integer.TYPE);
    assertFalse(int.class == Integer.class);
    // e) XXXClassLoader.loadClass()
    // Class.forName() -> use XXXClassLoader.loadClass
    assertDoesNotThrow(() -> {
      ClassLoader ld = ClassLoader.getSystemClassLoader(); // class found
      Class<?> csub = ld.loadClass("korhal.lang.Sub");
      assertTrue(csub != null);
      assertTrue(csub == Sub.class);
    });
    assertThrows(ClassNotFoundException.class, () -> {
      ClassLoader ld = ClassLoader.getPlatformClassLoader(); // not found
      Class<?> csub = ld.loadClass("korhal.lang.Sub");
      assertTrue(csub != null);
      assertTrue(csub == Sub.class);
    });
  }

  /**
   *
   * Bootstrap ClassLoader (native)
   * ↑
   * Platform ClassLoader (loads java.*, javax.* core libs)
   * ↑
   * Application ClassLoader (loads your classes from classpath)
   * ↑
   * System ClassLoader = Application ClassLoader (by default)
   */
  @Test
  public void class_loader_test() {
    assertDoesNotThrow(() -> {
      ClassLoader ld = ClassLoader.getSystemClassLoader(); // class found
      Class<?> csub = ld.loadClass("korhal.lang.Sub");
      assertTrue(csub != null);
      assertTrue(csub == Sub.class);
    });
    assertThrows(ClassNotFoundException.class, () -> {
      ClassLoader ld = ClassLoader.getPlatformClassLoader(); // not found
      Class<?> csub = ld.loadClass("korhal.lang.Sub");
      assertTrue(csub != null);
      assertTrue(csub == Sub.class);
    });
  }

  /**
   * instanceof and Class.isInstance()
   *
   * obj instanceof TypeName — Compile-time check
   * Test if obj is of a type which <? extends TypeName>, i.e. equals to
   * TypeName, the subtype of TypeName, the type implements TypeName
   * (interface)
   *
   * clazz.isInstance(obj) — Runtime check
   *
   */
  @Test
  public void instanceof_Class_isInstance_test() {
    // 1. obj instanceof TypeName
    String s1 = "Hello";
    String s2 = new String("world");
    assertTrue(s1 instanceof String);
    assertTrue(s2 instanceof String);
    assertTrue("" instanceof String);
    assertTrue("" instanceof CharSequence); // interface implementation
    assertTrue("" instanceof Serializable); // interface implementation
    assertTrue(Integer.valueOf(0) instanceof Integer);
    assertTrue(Integer.valueOf(0) instanceof Number); // class inheritance
    assertTrue(new String[] {} instanceof String[]);
    assertTrue(new Integer[] {} instanceof Integer[]);
    assertTrue(new int[] {} instanceof int[]);

    assertTrue(String.class instanceof Class);
    assertTrue(String.class instanceof Object);
    assertTrue(String[].class instanceof Class);
    assertTrue(Integer[].class instanceof Object);
    assertTrue(int[].class instanceof Class);
    assertTrue(Class.class instanceof Class);
    assertTrue(Class.class instanceof Object);
    assertTrue(Object.class instanceof Object);
    assertTrue(Object.class instanceof Class);

    // NOTE: instanceof with null always returns false
    assertFalse(null instanceof String);
    assertFalse(null instanceof Class);
    assertFalse(null instanceof Object);
    assertFalse(null instanceof String[]);
    assertFalse(null instanceof int[]);

    // 2. clazz.isInstance(obj)
    assertTrue(String.class.isInstance(s1));
    assertTrue(String.class.isInstance(""));
    assertTrue(CharSequence.class.isInstance(""));
    assertTrue(Serializable.class.isInstance(""));
    assertTrue(Integer.class.isInstance(Integer.valueOf(0)));
    assertTrue(Number.class.isInstance(Integer.valueOf(0)));

    assertTrue(String[].class.isInstance(new String[] {}));
    assertTrue(Integer[].class.isInstance(new Integer[] {}));
    assertTrue(Number[].class.isInstance(new Integer[] {}));
    assertTrue(int[].class.isInstance(new int[] {}));
    assertTrue(Class.class.isInstance(Class.class));
    assertTrue(Class.class.isInstance(Object.class));
    assertTrue(Object.class.isInstance(Class.class));
    assertTrue(Object.class.isInstance(Object.class));

    assertFalse(String.class.isInstance(null));

  }

  /**
   * Compare two class
   *
   * Two reference types are the same compile-time type if they are:
   * 
   */
  @Test
  public void class_compare_test() {
    Class<? extends Object> c1 = String[].class;
    Class<? extends Object> c2 = Object[].class;
    assertTrue(c1 != c2);
    assertTrue((Object) c1 != (Object) c2);
    assertTrue(c1 instanceof Object);
    assertTrue(c1 instanceof Class);
    Class<? extends Object> cs = String.class;
    Class<? extends Object> ci = Integer.class;
    assertTrue(cs instanceof Object);
    assertTrue(cs instanceof Class);
    assertTrue(ci instanceof Object);
    assertTrue(ci instanceof Class);
    assertTrue((Object) cs != (Object) ci);

    /**
     * getClass() inherited from Object class returns the {@code Class} object
     * that represents the runtime class of the caller object.
     * {@code public final native Class<?> getClass();}
     *
     * The actual result type that is inferred by the compiler is
     * {@code Class<? extends |X|>}, where |X| means the erasure of static type X.
     *
     * static type = the declared type at compile time
     * erasure = the version that has all generic info removed
     *
     * E.g. List<String> (compile time) -> List (runtime)
     *
     * The erasure of a generic type is the type that survives at runtime.
     *
     * E.g. Integer, no generic, no erasure, no cast
     * Integer i = 0;
     * Class<? extends Integer> c = i.getClass();
     *
     * E.g. List<Integer> -> List
     * List<Integer> li = new ArrayList<>();
     * @SuppressWarnings("rawtypes")
     * Class<? extends List> cl0 = l.getClass();
     *
     * You can assign upcast (from specific to general) freely:
     * E.g. from Class<String> to Class<?>
     * Class<?> raw = String.class;
     *
     * You cannot assign downcast (from general to specific) without a explict
     * cast
     * E.g. from Class<?> to Class<String>
     * Class<String> cs = "Hello".getClass(); // error w/o casting
     * Class<String> cs = (Class<String>)"Hello".getClass(); // OK
     *
     */

    /**
     * T obj = new T();
     * obj.getClass() == T.class
     */

    // 1. No generic, no erasure, no cast.
    Integer i = 0;
    Class<? extends Integer> ci0 = i.getClass();
    assertTrue(Integer.class == ci0);
    assertEquals("java.lang.Integer", ci0.getName());
    Class<? extends Integer> ci1 = Integer.class;
    assertEquals("java.lang.Integer", ci1.getName());

    // 2. Erasure of List<String> and inferred as Class<? extends List>
    List<String> l = new ArrayList<>();
    @SuppressWarnings("rawtypes")
    Class<? extends List> cl0 = l.getClass();
    assertTrue(ArrayList.class == cl0);
    assertEquals("java.util.ArrayList", cl0.getName());

    Class<?> cs1 = "Hello".getClass();
    Class<?> ci2 = Integer.valueOf(0).getClass();
    // Class<String> cs2 = "Hello".getClass(); // Compilation Error

    // OK when explicit cast and no warning if @SuppressWarnings("unchecked")
    @SuppressWarnings("unchecked")
    Class<String> cs2 = (Class<String>) "Hello".getClass();

    Class<? extends String> cs3 = "Hello".getClass(); // OK
    assertTrue(cs1 == String.class);
    assertTrue(cs2 == String.class);
    assertTrue(cs3 == String.class);
    assertTrue(ci2 == Integer.class);

    assertEquals("java.lang.Class", Class.class.getName());

    // A reifiable type is a type whose information is fully available at
    // runtime — including all type arguments (if any).
    // Java's type erasure removes generic type information at compile time,
    // most parameterized types like List<String> are not reifiable.

    // T.class only works with reifiable types
    // List.class // ✅ valid
    // String.class // ✅ valid
    // int[].class // ✅ valid

    // T.class doesn't work with types that is not reifiable
    // List<String>.class // ❌ Compilation error
    // Class<Integer>.class // ❌ Compilation error

    // T.class  ❌ Compilation error (unless T is reifiable)

    // Class<?> with unbounded wildcard is reifiable
    // Class<? extends T> with bounded wildcard is also treated as reifiable
    // for operations like `instanceof`
    // Class<Integer>, Class<String> with concrete type parameter is
    // not-reifiable, cannot works with `instanceof`

    Class<?> c000 = List.class;
    Class<?> c001 = String.class;
    Class<?> c002 = int[].class;
    @SuppressWarnings("rawtypes")
    Class c003 = Integer.class;
    // Class<?> c004 = Class<Integer>.class; // ❌ Compilation error

    Integer i000 = 0;

    assertTrue(i000 instanceof Integer);
    assertTrue(i000 instanceof Number);
    assertTrue(i000 instanceof Object);
    assertTrue(i000 instanceof Comparable);
    assertTrue(i000 instanceof Comparable<Integer>);

    // In the context of generics, there is notion of
    // containment of type arguments when dealing with wildcard.
    // List<? extends S> is contained by List<? extends T>, if S <: T
    // (i.e., S is a subtype of T)
    assertTrue(i000.getClass() instanceof Class<?>);                  // ✅
    assertTrue(i000.getClass() instanceof Class<? extends Number>);   // ✅
    assertTrue(i000.getClass() instanceof Class<? extends Integer>);  // ✅
    // assertTrue(i000.getClass() instanceof Class<Integer>);         // ❌ COMPILATION ERROR

    List<Integer> l000 = new ArrayList<>();
    assertTrue(l000.getClass() instanceof Class<?>);
    assertTrue(l000.getClass() instanceof Class<? extends List>);

  }
}

/**
 * Test result:
 * [INFO] Running korhal.lang.ReflectionTest
 * [INFO] [stdout] priv: reflect_override_access_control_test
 * [INFO] [stdout] pub: reflect_override_access_control_test
 * [INFO] [stdout] pub: Hello, this is sub
 * [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.061
 * s -- in korhal.lang.ReflectionTest
 */
