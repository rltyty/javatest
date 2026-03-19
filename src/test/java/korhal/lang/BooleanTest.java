package korhal.lang;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import korhal.helper.BaseTest;

public class BooleanTest extends BaseTest {

  /**
   * Negative: !t == (t ^ true)
   */
  @Test
  public void test_negative_xor_true() {
    boolean s = false;
    boolean t = false;
    s = !s;
    t ^= true;
    assertTrue(s);
    assertTrue(t);
    s = !s;
    t ^= true;
    assertFalse(s);
    assertFalse(t);
  }
}
