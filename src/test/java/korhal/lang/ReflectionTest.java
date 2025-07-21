package korhal.lang;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Super {
  public void pub(String s) {
    System.out.println("pub: " + s);
  }

  private void priv(String s) {
    System.out.println("priv: " + s);
  }
}

class Sub extends Super {}

public class ReflectionTest {
  @Test
  public void sub_call_sup_pub_test() {
    Sub sub = new Sub();
    sub.pub("Hello, this is sub"); // NO: sub.priv(); invisible to sub
  }

  /**
   * Sub class can access Super class's private method via java reflection
   * mechanism. Not suggested.
   *
   * @throws Exception
   */
  @Test
  public void reflect_override_access_control_test() throws Exception {
    Sub sub = new Sub();
    Method m = Super.class.getDeclaredMethod("priv", String.class);
    m.setAccessible(true);
    m.invoke(sub, "reflect_override_access_control_test");
    Method m2 = Super.class.getDeclaredMethod("pub", String.class);
    m2.setAccessible(true);
    m2.invoke(sub, "reflect_override_access_control_test");
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
    // d) Primitive Type Wrapper's TYPE field
    assertTrue(int.class == Integer.TYPE);
    // e) XXXClassLoader.loadClass()
    // Class.forName() -> use XXXClassLoader.loadClass
    assertDoesNotThrow(() -> {
      ClassLoader ld = ClassLoader.getSystemClassLoader();    // class found
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
Bootstrap ClassLoader (native)
   ↑
Platform ClassLoader (loads java.*, javax.* core libs)
   ↑
Application ClassLoader (loads your classes from classpath)
   ↑
System ClassLoader = Application ClassLoader (by default)
   */
  @Test
  public void class_loader_test() {
    assertDoesNotThrow(() -> {
      ClassLoader ld = ClassLoader.getSystemClassLoader();    // class found
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
}

/**
 * Test result:
[INFO] Running korhal.lang.ReflectionTest
[INFO] [stdout] priv: reflect_override_access_control_test
[INFO] [stdout] pub: reflect_override_access_control_test
[INFO] [stdout] pub: Hello, this is sub
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.061 s -- in korhal.lang.ReflectionTest
 */
