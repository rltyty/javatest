package korhal.misc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class SyntaxSugarTest {

  @Test
  public void switch_test() {
    String player = "Messi";
    switch (player) {
      case "Pele":
      case "Maradona":
        System.out.println(player + " is a former GOAT.");
        break;
      case "Messi":
        System.out.println(player + " is the GOAT.");
        break;
      default:
        System.out.println(player + " is irrelevant to GOAT topic.");
        break;
    }
  }

  @Test
  public void generic_test() {
    Map<String, Integer> map = new HashMap<>();
    map.put("Messi", 10);
    map.put("Crespo", 9);
    map.put("Alvarez", 19);
  }

  @Test
  public void boxing_test() {
    int i = 10;
    Integer n = i;
  }

  @Test
  public void numeric_literal_test() {
    int __ = 1_000_000;               // desugar() -> 1000000
    int __10 = 1_000_000_0;           // desugar() -> 10000000
    int ___ = 1_000_000_000;          // desugar() -> 1000000000
    assertEquals(1000000, __);
    assertEquals(10000000, __10);
    assertEquals(1000000000, ___);
  }
}
