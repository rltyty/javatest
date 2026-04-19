package korhal.algorithm.str;

import java.util.ArrayList;
import java.util.List;

public class KMP_Me2 implements KMP {
  public List<Integer> kmp(String s, String p) {
    if (s == null || p == null)
      return null;
    if (p.length() == 0 || s.length() < p.length())
      return List.of();
    List<Integer> hits = new ArrayList<>();
    int[] prefix = prefix(p);
    int i = 0, j = 0;
    while (i < s.length()) {
      if (s.charAt(i) == p.charAt(j)) {
        i++;
        j++;
        if (j == p.length()) {
          hits.add(i - j);
          j = prefix[j];
        }
      } else {
        j = prefix[j];
        if (j == -1) {
          i++;
          j = 0;
        }
      }
    }
    return hits;
  }

  /**
   * Builds the KMP failure (prefix) table for the pattern string. f[i]
   * represents the length of the longest proper prefix of p[0..i-1] that is
   * also a suffix, where "proper" means the prefix is strictly shorter than
   * p[0..i-1] itself, i.e. it does not include p[i-1]. On a mismatch at index i
   * in the pattern, we fall back to f[i].
   *
   * However, we should add a mismatch constraint `p[f[i]] != p[i]` to avoid
   * redundant comparisons. t[i] will finally be returned as a "STRONG" KMP
   * failure table.
   *
   * <p>
   * NOTE: Mnemonic: f[i] -> examine the first i chars p[0..i-1] -> longest
   * proper prefix-suffix match of length at most i-1 -> t[i], meets
   * constraint that `p[t[i]] != p[i]`
   *
   * // fmt:off
   * i    0   1   2   3   4   5   6   7   8   9
   * p[i] A   B   A   C   A   B   A   D   C
   * t[i] -1  0   -1  1   -1  0   1   3   0   0
   * len      0   0   1   0   1   2   3   0   0
   * // fmt:on
   *
   * @param p the pattern string to preprocess
   * @return failure table of length p.length() + 1, where t[0] = -1 (sentinel)
   */
  private int[] prefix(String p) {
    int m = p.length();
    if (m == 0)
      return new int[] {-1};
    int[] t = new int[m + 1];
    t[0] = -1;
    int j = 0;        // current longest proper prefix/suffix overlap length
    int i = 1;
    while (i < m) {
      if (p.charAt(i) == p.charAt(j)) {
        t[i] = t[j];  // skip redundant fallback, inherit t[j]
      } else {
        t[i] = j;     // record j as fallback, then find new j
        for (; j >= 0 && p.charAt(i) != p.charAt(j); j = t[j]);
      }
      j++;            // index to length (+1)
      i++;
    }
    t[i] = j;
    return t;
  }

}
