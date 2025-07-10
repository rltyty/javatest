package korhal.utils;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class StopwatchExtension implements BeforeTestExecutionCallback,
    AfterTestExecutionCallback {

  private static final String START_TIME = "start time";
  private static final String ACTIVE_PROP = "stopwatch";

  private boolean isActive() {
    String v = System.getProperty(ACTIVE_PROP, "N");
    return v.equalsIgnoreCase("Y");
  }

  @Override
  public void beforeTestExecution(ExtensionContext context) throws Exception {
    if (isActive())
      context.getStore(ExtensionContext.Namespace.create(context.getRequiredTestMethod()))
          .put(START_TIME, System.nanoTime());
  }

  @Override
  public void afterTestExecution(ExtensionContext context) throws Exception {
    if (isActive()) {
      ExtensionContext.Store store = context
          .getStore(ExtensionContext.Namespace.create(context.getRequiredTestMethod()));
      long startTime = store.remove(START_TIME, long.class);
      long duration = System.nanoTime() - startTime;
      long s = duration / 1_000_000_000;
      long ms = (duration % 1_000_000_000) / 1_000_000;
      long us = (duration % 1_000_000) / 1_000; 
      long ns = duration % 1_000; 
      System.out.printf(TestUtils.RED + "%-35s" + TestUtils.RESET + ": %4d s %3d ms %3d µs %3d ns\n",
          context.getDisplayName(), s, ms, us, ns);
    }
  }
}
