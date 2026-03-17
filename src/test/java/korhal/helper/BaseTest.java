package korhal.helper;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TimerExtension.class)
public abstract class BaseTest {
  // Verbose mode
  private static final boolean VERBOSE = Boolean.getBoolean("test.verbose");
  // Time consuming tests enabled
  private static final boolean SLOW_TESTS_ENABLED =
    Boolean.getBoolean("test.slow.tests.enabled");

  public static boolean isSlowTestsEnabled() {
    return SLOW_TESTS_ENABLED;
  }

  public static boolean isVerbose() {
    return VERBOSE;
  }

  @BeforeAll
  public static void setUpAll() {
    // System.out.println("BaseTest: Global initialization.");
  }

  @BeforeEach
  protected void setUp() {
    // System.out.println("BaseTest: Case initialization.");
  }

  @AfterEach
  protected void tearDown() {
    // System.out.println("BaseTest: Clean work for this case.");
  }

  @AfterAll
  public static void tearDownAll() {
    // System.out.println("BaseTest: Global clean work.");
  }

}
