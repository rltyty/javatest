package korhal.lang;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.AbstractList;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

/**
 * - Subtyping (`subtype <: supertype`) is a **reflexive** and **transitive**
 * relation on two types, include:
 *   - identity
 *   - subclassing
 *   - interface extension
 *   - interface implementation
 * - Subtyping enables upcasting.
 * - To examine subtyping relation of all the above cases
 *   Use {@code supertype.isAssignableFrom(subtype) } to examine subtyping.
 */
public class SubtypingTest {

  /**
   * Subtyping by identity.
   *
   * Every type is a subtype of itself, by reflexivity of subtyping.
   *
   * A.class == B.class
   *
   * NOTE:
   * Within a given class loader, a class (by name) has exactly one
   * Class object at runtime.
   */
  @Test
  public void identity_test() {
    Integer i00 = 0;
    Integer i01 = 1;
    assertTrue(i00.getClass() == i01.getClass());
    assertTrue(Integer.class == i00.getClass());

    // subtype check
    assertTrue(i00.getClass().isAssignableFrom(Integer.class));
    assertTrue(Integer.class.isAssignableFrom(i01.getClass()));

    Number n00 = 3; // 3, autoboxing to an Integer
    assertTrue(Integer.class == n00.getClass());
    assertEquals("java.lang.Integer", n00.getClass().getName());
    assertNotEquals(Number.class, Integer.class);

    // subtype check
    assertTrue(Object.class.isAssignableFrom(Object.class));
    assertTrue(Integer.class.isAssignableFrom(Integer.class));
    assertTrue(Number.class.isAssignableFrom(n00.getClass()));
    assertTrue(Integer.class.isAssignableFrom(n00.getClass()));
    assertTrue(Number.class.isAssignableFrom(Integer.class));

    /**
     * NOTE: Number is an abstract class, no instantiation.
     * So no object.getClass() will return Number.class
     */
  }

  /**
   * Subtyping by direct and indirect class inheritance.
   *
   * Check direct class inheritance:
   * A.class == B.class.getSuperclass()
   */
  @Test
  public void subclassing_test() {
    assertTrue(Number.class == Integer.class.getSuperclass());
    assertTrue(Object.class == Number.class.getSuperclass());
    assertTrue(Object.class != Integer.class.getSuperclass());
    assertTrue(Object.class == int[].class.getSuperclass());

    assertNull(Object.class.getSuperclass()); // Object (root) has no super class

    // subtype check
    assertTrue(Number.class.isAssignableFrom(Integer.class));
    assertTrue(Object.class.isAssignableFrom(Number.class));
    assertTrue(Object.class.isAssignableFrom(Integer.class));
    assertTrue(Object.class.isAssignableFrom(int[].class));
    assertTrue(Object.class.isAssignableFrom(Object.class));

    interface Intf {}
    interface IntfSub extends Intf {}
    assertEquals("korhal.lang.SubtypingTest$1Intf", Intf.class.getName());
    assertEquals("korhal.lang.SubtypingTest$1IntfSub", IntfSub.class.getName());
    assertNull(IntfSub.class.getSuperclass());  // interface has no super class
                                                // but has super type

    // subtype check
    assertTrue(Intf.class.isAssignableFrom(IntfSub.class));

    // Java has
    // transitive subclassing
    assertTrue(isSubclass(Integer.class, Number.class));
    assertTrue(isSubclass(Integer.class, Object.class));
    assertTrue(isSubclass(ArrayList.class, AbstractList.class));

    // subtype check
    assertTrue(Number.class.isAssignableFrom(Integer.class));
    assertTrue(Object.class.isAssignableFrom(Integer.class));
    assertTrue(AbstractList.class.isAssignableFrom(ArrayList.class));
  }

  /*
   * Check indirect class inheritance.
   */
  private boolean isSubclass(Class<?> possibleSub, Class<?> possibleSuper) {
    if (possibleSub == null || possibleSuper == null || possibleSub == possibleSuper) return false;
    Class<?> clazz = possibleSub;
    for (; clazz != null && clazz != possibleSuper; clazz = clazz.getSuperclass())
      ;
    if (clazz != null) return true;
    return false;
  }

  /**
   * Subtyping by interface extension
   */
  @Test
  public void inteface_extension_test() {
    interface Intf {}
    interface IntfSub extends Intf {}

    // subtype check
    assertTrue(Intf.class.isAssignableFrom(IntfSub.class));
    assertFalse(IntfSub.class.isAssignableFrom(Intf.class));

    // Every interface is a subtype of Object, but not a subclass of Object
    assertTrue(Object.class.isAssignableFrom(Intf.class));
    assertTrue(Object.class.isAssignableFrom(IntfSub.class));
  }

  /**
   * Subtyping by interface implementation.
   */
  @Test
  public void interface_implementation_test() {
    interface Intf {}
    interface IntfSub extends Intf {}
    class ClassA implements Intf {}
    class ClassB implements IntfSub {}
    class ClassC extends ClassA {}
    class ClassD extends ClassB {}

    assertTrue(Intf.class.isAssignableFrom(ClassA.class));
    assertTrue(Intf.class.isAssignableFrom(ClassB.class));
    assertTrue(Intf.class.isAssignableFrom(ClassC.class));
    assertTrue(Intf.class.isAssignableFrom(ClassD.class));

    assertTrue(Intf.class.isAssignableFrom(ClassA.class));
    assertTrue(Intf.class.isAssignableFrom(ClassB.class));
    assertTrue(Intf.class.isAssignableFrom(ClassC.class));
    assertTrue(Intf.class.isAssignableFrom(ClassD.class));
  }

  /**
   * if (Super.class.isAssignableFrom(obj.getClass())) {
   *  assert obj instanceof Super;  // compile time check: Super is known
   * }
   *
   * Class<?> c;
   * if (c.isAssignableFrom(obj.getClass()) {
   *  assert c.isInstance(obj);     // runtime check: type of c is dynamic
   * }
   */
  private void assert_isInstance_isAssignableFrom(Class<?> c, Object o) {
    assertTrue(c.isAssignableFrom(o.getClass()));
    assertTrue(c.isInstance(o));
  }

  @Test
  public void instanceof_isAssignableFrom_test() {
    assert_isInstance_isAssignableFrom(Object.class, Integer.valueOf(0));
    assert_isInstance_isAssignableFrom(Number.class, Integer.valueOf(0));
    assert_isInstance_isAssignableFrom(Integer.class, Integer.valueOf(0));

    interface Intf {}
    interface IntfSub extends Intf {}
    class ClassA implements Intf {}
    class ClassB implements IntfSub {}
    class ClassC extends ClassA {}
    class ClassD extends ClassB {}

    ClassA a = new ClassA();
    ClassB b = new ClassB();

    assert_isInstance_isAssignableFrom(Intf.class,    a);
    assert_isInstance_isAssignableFrom(Intf.class,    b);
    assert_isInstance_isAssignableFrom(IntfSub.class, b);

    ClassC c = new ClassC();
    ClassD d = new ClassD();

    assert_isInstance_isAssignableFrom(Intf.class,    c);
    assert_isInstance_isAssignableFrom(Intf.class,    d);
    assert_isInstance_isAssignableFrom(IntfSub.class, d);
    assert_isInstance_isAssignableFrom(ClassA.class,  c);
    assert_isInstance_isAssignableFrom(ClassB.class,  d);
  }
}
