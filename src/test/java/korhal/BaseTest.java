package korhal;

import org.junit.jupiter.api.extension.ExtendWith;

import korhal.utils.StopwatchExtension;

@ExtendWith(StopwatchExtension.class)
public abstract class BaseTest {
  protected static boolean isSlowTestEnabled() {
    return "Y".equalsIgnoreCase(System.getProperty("enable.slow.test"));
  }
}
