package korhal.algorithm.str;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/** Problem: String Finding (https://dmoj.ca/problem/bf4) */
public class KMP_Single2 {

  public int kmp(String s, String p) {
    if (s.length() < p.length()) return -1;
    int[] prefix = prefix(p);
    int i = 0, j = 0;
    while (i < s.length()) {
      if (s.charAt(i) == p.charAt(j)) {
        i++;
        j++;
        if (j == p.length()) {
          return i - j;
        }
      } else {
        j = prefix[j];
        if (j == -1) {
          i++;
          j = 0;
        }
      }
    }
    return -1;
  }

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

  public static void main1(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String s = br.readLine();
    String t = br.readLine();
    KMP_Single2 m = new KMP_Single2();
    System.out.println(m.kmp(s, t));
  }

  public static void main(String[] args) throws IOException {
    System.out.println(args[0] + " " + args[1]);
    String s = args[0];
    String t = args[1];
    KMP_Single2 m = new KMP_Single2();
    System.out.println(m.kmp(s, t));
  }

}
