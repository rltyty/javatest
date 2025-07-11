package korhal.lang;

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
}

/**
 * Test result:
[INFO] Running korhal.lang.ReflectionTest
[INFO] [stdout] priv: reflect_override_access_control_test
[INFO] [stdout] pub: reflect_override_access_control_test
[INFO] [stdout] pub: Hello, this is sub
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.061 s -- in korhal.lang.ReflectionTest
 */
