package korhal.misc;

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
}
