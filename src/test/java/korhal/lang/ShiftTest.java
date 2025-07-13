package korhal.lang;

import static korhal.helper.TestUtils.print_dec_bin32;
import static korhal.helper.TestUtils.print_sep;
import static korhal.helper.TestUtils.to32BitBinaryString;
import static korhal.helper.TestUtils.twosComplement;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import korhal.BaseTest;
 
public class ShiftTest extends BaseTest {

  /**
   * Bitwise NOT (~): Just flip all the bits. 0 -> 1, 1 -> 0
   * Mathematically, ~ x = -(x + 1)
   */
  @Test
  public void test_bitwise_not() {
    assertEquals(~0, -1);
    assertEquals(~-1, 0);
    assertEquals(~127, -128);
    assertEquals(~-128, 127);
    assertEquals(~ -5, 4);
    assertEquals(~ 6, -7);
    assertEquals(~Integer.MAX_VALUE, Integer.MIN_VALUE);
    assertEquals(~Integer.MIN_VALUE, Integer.MAX_VALUE);
  }

  /**
   * Bit Shift Modulo Behavior
   */
  @Test
  public void test_shit_modulo()
  {
    assertEquals(1, 1 << 32);                       // int: 32 & 0x1F = 0
    assertEquals(0x100000000L, 1L << 32);           // long: 32 & 0x3F = 32
    assertEquals(0x100000000L, 1L << 32 - 128);     // 1 << (-96)
                                                    // -96 & 0x3F = 32
  }

  /**
   * In 32-bit 2's complement:
   * 1. All operations wrap around modulo 2³².
   * 2. x + twosComplement(x) === 0
   * 3. twosComplement(x) == ~ x + 1
   *    twosComplement(x) == (x == Integer.MIN_VALUE) ? x : -x
   */
  @Test
  public void test_twosComplement() {
    assertEquals(0, twosComplement(0));
    assertEquals(Integer.MIN_VALUE, twosComplement(Integer.MIN_VALUE));

    assertEquals(-Integer.MAX_VALUE, twosComplement(Integer.MAX_VALUE));
    assertEquals(-1, twosComplement(1));
    assertEquals(-2, twosComplement(2));

    assertEquals(0, 0 + twosComplement(0));
    assertEquals(0, 1 + twosComplement(1));
    assertEquals(0, 2 + twosComplement(2));
    assertEquals(0, -1 + twosComplement(-1));
    assertEquals(0, -2 + twosComplement(-2));
    assertEquals(0, Integer.MAX_VALUE + twosComplement(Integer.MAX_VALUE));
    assertEquals(0, Integer.MIN_VALUE + twosComplement(Integer.MIN_VALUE));
  }

  private void unsignedLong_twosComplement(int x) {
    assertEquals(Integer.toUnsignedLong(x), (x >= 0) ? x : (1L << 32) + x);
  }

  @Test
  public void test_unsigned_long() {
    unsignedLong_twosComplement(5);
    unsignedLong_twosComplement(-5);
    unsignedLong_twosComplement(Integer.MIN_VALUE);
    unsignedLong_twosComplement(Integer.MAX_VALUE);
  }

  @Test
  public void test_floor_ceil() {
    /* floor toward negative infinity */
    assertEquals(-64, Math.floor(-63.6));
    /* ceil toward positive infinity */
    assertEquals(-63, Math.ceil(-63.3));
  }

  /**
   * /: truncated division (rounds toward 0)
   */
  @Test
  public void test_division() {
    // For x / y:
    // 1. Compute abs(x) / abs(y)
    // 2. Truncate the fraction part
    // 3. Apply the sign: (x ^ y) >= 0 ? 1 : -1
    assertEquals(3, 7 / 2);
    assertEquals(-3, -7 / 2);
    assertEquals(-1, Math.signum(-3));
    assertEquals(1, Math.signum(3));

    assertEquals(-7, -127 / 16);
    assertEquals(-7, Math.floor(-127 / 16));
    assertEquals(-8, Math.floor(-127 / ((double) 16)));
  }

  /**
   * Test floorDiv(x, y) == floor(double(x) / y)
   */
  private void floorDiv_floorDoubleDiv(int x, int y) {
    assertEquals(Math.floorDiv(x, y), Math.floor((double) x / y));
  }

  @Test
  public void test_floorDiv() {
    floorDiv_floorDoubleDiv(1, 2);
    floorDiv_floorDoubleDiv(7, 3);
    floorDiv_floorDoubleDiv(-7, 3);
    floorDiv_floorDoubleDiv(7, -3);
    floorDiv_floorDoubleDiv(7, -4);
    floorDiv_floorDoubleDiv(-7, 4);
    floorDiv_floorDoubleDiv(-7, -4);
    floorDiv_floorDoubleDiv(-128, 32);
    floorDiv_floorDoubleDiv(-128, 31);
    floorDiv_floorDoubleDiv(-128, 17);
    floorDiv_floorDoubleDiv(-127, 16);
    assertEquals(-2, Math.floorDiv(7, -4));
    assertEquals(1, Math.floorDiv(7, 4));
  }

  @Test
  public void test_limits_overflow() {
    assertEquals(-2147483648, Integer.MIN_VALUE);
    assertEquals(2147483647, Integer.MAX_VALUE);

    assertEquals(Integer.MIN_VALUE - 1, Integer.MAX_VALUE);
    assertEquals(Integer.MAX_VALUE + 1, Integer.MIN_VALUE);

    assertEquals(
        "10000000000000000000000000000000", to32BitBinaryString(Integer.MIN_VALUE));
    assertEquals(
        "01111111111111111111111111111111", to32BitBinaryString(Integer.MAX_VALUE));

    assertEquals(
        "00000000000000000000000000000111", to32BitBinaryString(7));
    assertEquals(
        "11111111111111111111111111111001", to32BitBinaryString(-7));
    assertEquals(
        "00000000000000000000000000000000", to32BitBinaryString(-7 + 7));
  }

  /**
   * Arithmetic right shift (floored division, rounds towards -∞),
   * x >> n == Math.floorDiv(x, 1 << n) == Math.floor((double)x / (1 << n))
   * 1. Floored division, rounds toward -∞.
   * 2. Preserves the sign.
   * 3. Fills new leftmost with the sign bit.
   */
  @Test
  public void test_arithmetic_right_shift() {
    assertEquals("11111111111111111111111110000000", to32BitBinaryString(-128));
    assertEquals("11111111111111111111111111000000", to32BitBinaryString(-128 >> 1));
    assertEquals("11111111111111111111111111000000", to32BitBinaryString(-128 / 2));
    assertEquals(-128 >> 1, -128 / 2);

    assertEquals("11111111111111111111111110000001", to32BitBinaryString(-127));
    assertEquals("11111111111111111111111111000000", to32BitBinaryString(-127 >> 1));
    assertEquals("11111111111111111111111111000001", to32BitBinaryString(-127 / 2));
    assertEquals(Math.floorDiv(-127, 2), -127 >> 1);
    assertEquals(-64, -127 >> 1);
    assertEquals(-127 / 2, -63);
    assertEquals(Math.floorDiv(-127, 1 << 4), -127 >> 4);
    assertEquals(Math.floor((double) -127 / (1 << 4)), -127 >> 4);
    assertEquals(-2, -7 >> 2);
    assertEquals(-4, -7 >> 1);
    assertEquals(-4, -8 >> 1);
  }

  /**
   * Logical right shift
   * 1. Treat the number as unsigned, always non-negative
   * 2. Fills new leftmost with 0
   * 3. x >>> n == Integer.toUnsignedLong(x) / (1 << n)
   *    x >>> n == (0xffffffffL & x) / (1 << n)
   */
  @Test
  public void test_logical_right_shift() {

    //  1073741824 (dec), 01000000000000000000000000000000 (bin)
    // -2147483647 (dec), 10000000000000000000000000000001 (bin)
    //  1073741824 (dec), 01000000000000000000000000000000 (bin)
    //        -128 (dec), 11111111111111111111111110000000 (bin)
    //        -127 (dec), 11111111111111111111111110000001 (bin)
    //  2147483584 (dec), 01111111111111111111111111000000 (bin)

    assertEquals("10000000000000000000000000000000", to32BitBinaryString(Integer.MIN_VALUE));
    assertEquals("01000000000000000000000000000000", to32BitBinaryString(Integer.MIN_VALUE >>> 1));
    assertEquals("10000000000000000000000000000001", to32BitBinaryString(Integer.MIN_VALUE+1));
    assertEquals("01000000000000000000000000000000", to32BitBinaryString((Integer.MIN_VALUE+1) >>> 1));
    assertEquals(Integer.MIN_VALUE >>> 1, (Integer.MIN_VALUE+1) >>> 1);

    assertEquals("11111111111111111111111110000000", to32BitBinaryString(-128));
    assertEquals("00111111111111111111111111100000", to32BitBinaryString(-128 >>> 2));
    assertEquals((0xffffffffL & -128) / (1 << 2), -128 >>> 2);
    assertEquals(-128 >>> 2, ((1L << 32) - 128) / 4);
    assertEquals(1073741792, -128 >>> 2);
    assertEquals(1073741792, -127 >>> 2);

    assertEquals(0x100000000L, 1L<<32);
    assertEquals(1L << 32, Integer.toUnsignedLong(-128) + twosComplement(-128));
  }

  /**
   * Fills new rightmost with 0
   * The left shift performs (x * 2^n) % 2^32, regardless of the sign of x.
   */
  @Test
  public void test_left_shift() {
    assertEquals(0, 0 << 5);
    assertEquals(0, Integer.MIN_VALUE << 1);
    assertEquals(-2, Integer.MAX_VALUE << 1);
    assertEquals(2, (Integer.MIN_VALUE+1) << 1);
    assertEquals(16, 1 << 4);
    assertEquals(-16, -1 << 4);
  }


  public static void main(String[] args) {
    ShiftTest ts = new ShiftTest();
    print_sep("Right shift:");
    print_sep("Right arithmetic shift:");
    // assert "hello".equals("hell");
    // 1. Right shift, two choices
    // - Unsigned integers need logical shift (fills in 0) to stay
    // non-negative.
    // - Signed integers need arithmetic shift (fills in with the sign bit)
    // to maintain the sign. Replicate the sign bit to the right.

    // 1.1 arithmetic right shift (fill in with the sign, maintain the sign)
    // non-negative: fills in 0; negative: fills in 1

    // negative integers arithmetic right shift, toward negative infinity (-∞)
    // i.e. the absolute value rounds up
    print_dec_bin32(Integer.MIN_VALUE);
    print_dec_bin32(Integer.MIN_VALUE >> 1);
    print_dec_bin32(Integer.MIN_VALUE + 1);
    print_dec_bin32((Integer.MIN_VALUE + 1) >> 1);

    print_sep("", ' ');
    // negative number division goes to floor (in 0 direction)
    print_dec_bin32(-128);
    print_dec_bin32(-128 >> 1); // -64
    print_dec_bin32(-127);
    print_dec_bin32(-127 >> 1); // -64
    print_dec_bin32(-127 / 2);  // -63

    print_sep("", ' ');
    // non-negative integers arithmetic right shift goes to floor (in 0
    // direction)
    print_dec_bin32(Integer.MAX_VALUE);
    print_dec_bin32(Integer.MAX_VALUE >> 1);
    print_dec_bin32(127);
    print_dec_bin32(127 >> 1);
    // same to non-negative division goes to floor (in 0 direction)
    print_dec_bin32(127 / 2);

    /*
    ------------------------------  Right arithmetic shift:  ------------------------------
    -2147483648 (dec), 10000000000000000000000000000000 (bin)
    -1073741824 (dec), 11000000000000000000000000000000 (bin)
    -2147483647 (dec), 10000000000000000000000000000001 (bin)
    -1073741824 (dec), 11000000000000000000000000000000 (bin)

           -128 (dec), 11111111111111111111111110000000 (bin)
            -64 (dec), 11111111111111111111111111000000 (bin)
           -127 (dec), 11111111111111111111111110000001 (bin)
            -64 (dec), 11111111111111111111111111000000 (bin)
            -63 (dec), 11111111111111111111111111000001 (bin)

     2147483647 (dec), 01111111111111111111111111111111 (bin)
     1073741823 (dec), 00111111111111111111111111111111 (bin)
            127 (dec), 00000000000000000000000001111111 (bin)
             63 (dec), 00000000000000000000000000111111 (bin)
             63 (dec), 00000000000000000000000000111111 (bin)
     */



    print_sep("Right logical shift:");
    // 1.2 logical right shift, only fills 0, doesn't main negative sign
    print_dec_bin32(Integer.MIN_VALUE);
    print_dec_bin32(Integer.MIN_VALUE >>> 1);
    print_dec_bin32(Integer.MIN_VALUE + 1);
    print_dec_bin32((Integer.MIN_VALUE + 1) >>> 1);
    print_dec_bin32(-128);
    print_dec_bin32(-127);
    print_dec_bin32(-127 >>> 1);
    /*
    ------------------------------  Right logical shift:  ------------------------------
    -2147483648 (dec), 10000000000000000000000000000000 (bin)
     1073741824 (dec), 01000000000000000000000000000000 (bin)
    -2147483647 (dec), 10000000000000000000000000000001 (bin)
     1073741824 (dec), 01000000000000000000000000000000 (bin)
           -128 (dec), 11111111111111111111111110000000 (bin)
           -127 (dec), 11111111111111111111111110000001 (bin)
     2147483584 (dec), 01111111111111111111111111000000 (bin)


     */
    // 2. Left shift, one choice, just fills 0 from right.
    print_sep("Left shift:");

    print_dec_bin32(Integer.MIN_VALUE);
    print_dec_bin32(Integer.MIN_VALUE << 1);
    print_dec_bin32(Integer.MIN_VALUE + 1);
    print_dec_bin32((Integer.MIN_VALUE + 1) << 1);
    print_dec_bin32(-128);
    print_dec_bin32(-127);
    print_dec_bin32(-127 << 1);
    // negative number division goes to floor (in 0 direction)
    print_dec_bin32(-127 * 2);
    /*
    ------------------------------  Left shift:  ------------------------------
    -2147483648 (dec), 10000000000000000000000000000000 (bin)
              0 (dec), 00000000000000000000000000000000 (bin)
    -2147483647 (dec), 10000000000000000000000000000001 (bin)
              2 (dec), 00000000000000000000000000000010 (bin)
           -128 (dec), 11111111111111111111111110000000 (bin)
           -127 (dec), 11111111111111111111111110000001 (bin)
           -254 (dec), 11111111111111111111111100000010 (bin)
           -254 (dec), 11111111111111111111111100000010 (bin)
    ------------------------------  Left shift:  ------------------------------
     */
  }
}
