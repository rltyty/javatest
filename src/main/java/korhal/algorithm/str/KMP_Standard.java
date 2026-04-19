package korhal.algorithm.str;

import java.util.ArrayList;
import java.util.List;

public class KMP_Standard implements KMP {

  // Theoretically, when t is an empty string "", [0, 1, ..., s.length()]
  // should be returned. However, searching for an empty string is meaningless,
  // so an empty list is returned for this edge case.
  public List<Integer> kmp(String s, String t) {
    int slen = s.length();
    int tlen = t.length();
    if (t.isEmpty() || slen < tlen)
      return List.of();
    List<Integer> hits = new ArrayList<>();
    int[] table = kmpTable(t);
    int i = 0; // position in s
    int j = 0; // position in t
    while (i < slen) {
      if (s.charAt(i) == t.charAt(j)) {
        i++;
        j++;
        if (j == tlen) {
          hits.add(i - tlen);
          j = table[j]; // use the extra entry for overlap
        }
      } else {
        j = table[j];
        if (j < 0) { // -1
          i++;
          j++; // 0
        }
      }
    }
    return hits;
  }

  private int[] kmpTable(String w) {
    if (w == null || w.length() == 0)
      return new int[] {-1};
    int wlen = w.length();
    int[] t = new int[wlen + 1]; // +1 for the extra entry
    t[0] = -1;
    // if (wlen == 0) return t; // never called with empty w
    int pos = 1;
    int cnd = 0;
    while (pos < wlen) {
      if (w.charAt(pos) == w.charAt(cnd)) {
        t[pos] = t[cnd];
      } else {
        t[pos] = cnd;
        while (cnd >= 0 && w.charAt(pos) != w.charAt(cnd))
          cnd = t[cnd];
      }
      pos++;
      cnd++;
    }
    t[pos] = cnd; // extra entry used after a full match
    return t;
  }
}
