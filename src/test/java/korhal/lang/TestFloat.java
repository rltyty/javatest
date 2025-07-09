package korhal.lang;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import korhal.TestBase;

public class TestFloat extends TestBase {

  @Test
  public void test_precision() {
    float a = 1.8f;
    float b = 1.9f;
    // System.out.println("1.9f - 1.8f = " + (b - a));
    // System.out.printf("1.9f - 1.8f = %.9f\n", (b - a));
    assertEquals("0.100000024", String.format("%.9f", (b-a)));
    assertNotEquals(0.1f, b - a); // expected: <0.1> but was: <0.100000024>
  }
}
