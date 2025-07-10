package korhal.lang;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import korhal.BaseTest;

public class TestBoxingUnboxing extends BaseTest {

  @EnabledIf("isSlowTestEnabled")
  @Test
  public void test_auto_boxing_performance() {
    Long sum = 0L;
    for (long i = 0; i <= Integer.MAX_VALUE; i++) sum += i;
  }

  @EnabledIf("isSlowTestEnabled")
  @Test
  public void test_primitive_type_performance() {
    // Use long instead of Long
    long sum = 0L;
    for (long i = 0; i <= Integer.MAX_VALUE; i++) sum += i;
  }

  @Test
  public void test_comparison() {
    assertTrue(1 == Integer.valueOf(1));
    assertTrue(Integer.valueOf(1).equals(1));
    assertTrue(Integer.valueOf(1).equals(1));
    Integer i1 = 33;
    Integer i2 = 33;
    assertTrue(i1 == i2);

    Float i3 = 333f;
    Float i4 = 333f;
    assertTrue(i3 != i4);
    assertTrue(i3.equals(i4));

    Double i5 = 1.2;
    Double i6 = 1.2;
    assertTrue(i5 != i6);
    assertTrue(i5.equals(i6));

    for (int i = 0; i < 1000; i++) {
      assertTrue(i == Integer.valueOf(i));
    }
  }
}
