package korhal.lang;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

public class BigIntegerTest {

  @Test
  public void big_int_test() {
    BigInteger a = new BigInteger("111111111111111111111111111111111", 10);
    BigInteger b = new BigInteger("1", 10);
    BigInteger c = new BigInteger("111111111111111111111111111111112", 10);
    assertEquals(c, a.add(b));
  }
}
