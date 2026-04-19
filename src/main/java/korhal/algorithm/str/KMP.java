package korhal.algorithm.str;

import java.util.List;

public interface KMP {
  public List<Integer> kmp(String s, String p);
  default int kmpSingle(String s, String p) { return -1; }
}
