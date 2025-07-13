package korhal.util;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class HashMapTest {
  @Test
  public void basic_test() {
    Map<Integer, String> m = new HashMap<>();
    m.put(10, "Messi");
    if (m.containsKey(10)) {
      System.out.println(m.get(10));
    }
  }
}
