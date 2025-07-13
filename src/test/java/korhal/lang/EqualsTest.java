package korhal.lang;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class EqualsTest {
  private static final class Clz {
    private int a;

    public Clz(int a) {
      this.a = a;
    }

    public int getA() {
      return a;
    }

    public void setA(int a) {
      this.a = a;
    }
  }

  private static final class ClzE {
    private int a;

    public ClzE(int a) {
      this.a = a;
    }

    public int getA() {
      return a;
    }

    public void setA(int a) {
      this.a = a;
    }

    /**
     * {@code @Override} is not a must but you should use it. It tells the compiler you are
     * overriding the method of the super class, here is {@code Object.equals(Object)}, if you
     * mistakenly write {@code public int equals(Object o);} without annotation {@code @Override},
     * the compiler treat it as overloading not overriding, and when {@code public boolean
     * Object.equals(o)} is called during runtime, the default one is used not your "overloading"
     * one.
     */
    @Override
    public boolean equals(Object o) {
      if (this == o) return true; // 1. Check reference equality fast
      if (!(o instanceof ClzE)) return false; // 2. Check type compatibility
      return this.a == ((ClzE) o).getA(); // 3. Compare significant fields
    }

    /**
     * Always override {@code hashCode()} when override {@code equals()}, esp.
     * when the class would be used in {@code HashSet}, {@code Hashtable}, etc.
     */
    @Override
    public int hashCode() {
      return Integer.hashCode(a);
    }
  }

  @Test
  public void test_array_equals_list_equals() {
    int[] a = new int[] {1, 2, 3};
    int[] b = new int[] {1, 2, 3};
    assertNotEquals(a, b); // [] equals(): shallow compare (ref ==)
    assertTrue(Arrays.equals(a, b));  // true when element is primitive
    assertArrayEquals(a, b);

    int[][] a2 = new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
    int[][] b2 = new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
    assertFalse((Arrays.equals(a2, b2))); // false when element is of Object
                                          // type w/o overriding equals()
                                          // equals() defaults to ref. ==
    assertArrayEquals(a2, b2);  // nested call of Arrays.equals()
                                // true when the final element is primitive

    assertTrue(Arrays.deepEquals(a2, b2)); // if elements are of primitive type
                                           // use ==, if elements are regular
                                           // Object, use the overriding
                                           // equals(), if elements are array
                                           // recursively call itself

    Clz[] a3 = new Clz[] {new Clz(1), new Clz(2), new Clz(3)};
    Clz[] b3 = new Clz[] {new Clz(1), new Clz(2), new Clz(3)};

    assertFalse(Arrays.equals(a3, b3)); // false when element is of Object
                                        // type w/o overriding equals()

    // expected error
    // assertArrayEquals(a3, b3);     // fail for the same reason as above

    assertFalse(Arrays.deepEquals(a3, b3)); // false for the same reason as

    ClzE[] a4 = new ClzE[] {new ClzE(1), new ClzE(2), new ClzE(3)};
    ClzE[] b4 = new ClzE[] {new ClzE(1), new ClzE(2), new ClzE(3)};

    assertTrue(Arrays.equals(a4, b4)); // true when element overrides equals()
    assertArrayEquals(a4, b4);         // true, for the same reason as above
    assertTrue(Arrays.deepEquals(a4, b4)); // true, same reason

    List<Integer> la = Arrays.asList(1, 2, 3);
    List<Integer> lb = Arrays.asList(1, 2, 3);
    assertEquals(la, lb);

    List<Clz> lc = Arrays.asList(new Clz(1), new Clz(2), new Clz(3));
    List<Clz> ld = Arrays.asList(new Clz(1), new Clz(2), new Clz(3));
    assertNotEquals(lc, ld);  // false

    List<ClzE> le = Arrays.asList(new ClzE(1), new ClzE(2), new ClzE(3));
    List<ClzE> lf = Arrays.asList(new ClzE(1), new ClzE(2), new ClzE(3));
    assertEquals(le, lf);     // true
  }

}
