package korhal.lang;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import korhal.BaseTest;

public class DoubleFloatTest extends BaseTest {

  @Test
  public void test_precision() {
    float a = 1.8f;
    float b = 1.9f;
    // System.out.println("1.9f - 1.8f = " + (b - a));
    // System.out.printf("1.9f - 1.8f = %.9f\n", (b - a));
    assertEquals("0.100000024", String.format("%.9f", (b-a)));
    assertNotEquals(0.1f, b - a); // expected: <0.1> but was: <0.100000024>
    assertNotEquals(0.3, 0.1 + 0.2);
    assertEquals(0.30000000000000004, 0.1 + 0.2);
  }

  @Test
  public void test_bigdecimal() {
    BigDecimal a = new BigDecimal("0.1");
    BigDecimal b = new BigDecimal("0.2");
    assertEquals(new BigDecimal("0.3"), a.add(b));

    BigDecimal c = new BigDecimal(0.1);
    BigDecimal d = new BigDecimal(0.2);
    assertNotEquals(new BigDecimal(0.3), c.add(d));
    // assertEquals(new BigDecimal(0.3), c.add(d)); // expected: <0.299999999999999988897769753748434595763683319091796875> but was: <0.3000000000000000166533453693773481063544750213623046875>
  }
}
