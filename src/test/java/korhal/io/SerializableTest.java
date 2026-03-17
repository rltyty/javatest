package korhal.io;

import java.io.Serializable;

import org.junit.jupiter.api.Test;

import korhal.helper.BaseTest;

public class SerializableTest extends BaseTest implements Serializable {
  // `serialver -classpath target/test-classes korhal.io.SerializableTest`
  // private static final long serialVersionUID = -6543866435090341062L;
  private static final long serialVersionUID = 1L;

  private String username;
  private transient String password; // 🚫 not serialized
  public static int group;           // 🚫 not serialized

  @Test
  public void stub() {
  }
}
