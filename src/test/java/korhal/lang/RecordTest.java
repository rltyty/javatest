package korhal.lang;

import org.junit.jupiter.api.Test;
import io.github.rltyty.jtkit.junit.helper.BaseTest;

public class RecordTest extends BaseTest {


  @Test
  public void test_record_1() {
    record Player(String firstname, String lastname, int age) { }
    Player messi = new Player("Lionel", "Messi", 39);
    System.out.println(messi); // Output: Player[firstname=Lionel, lastname=Messi, age=39]
    System.out.println(messi.firstname()); // Output: Lionel
  }
}
