package korhal.algorithm.str;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Timeout;
import io.github.rltyty.jtkit.junit.helper.BaseTest;
import io.github.rltyty.jtkit.junit.helper.annotation.FileData;
import io.github.rltyty.jtkit.junit.helper.annotation.ParseWith;
import io.github.rltyty.jtkit.junit.helper.loader.FlatFileLoader;
import io.github.rltyty.jtkit.junit.helper.parser.DataType;
import io.github.rltyty.jtkit.junit.helper.parser.Scenario;
import io.github.rltyty.jtkit.reflect.InvokePrivateHelper;

@Timeout(value = 1, unit = TimeUnit.SECONDS)
public class KMPTest extends BaseTest {

  public record Scenario_Failure_Tbl(
      @ParseWith(DataType.STRING) String t,
      @ParseWith(DataType.ARRAY_INTEGER) int[] expected
  ) implements Scenario {
  }

  @FileData(path = "kmp_std_ut.dat", loader = FlatFileLoader.class)
  public void kmp_UT_failure_table(Scenario_Failure_Tbl c) {
    captureTestCase(c);

    KMP alg = new KMP_Me2();
    int[] fb = (int[]) InvokePrivateHelper.invokePrivate(alg, "failure_tbl", c.t());
    assertArrayEquals(c.expected(), fb);
  }

  @FileData(path = "kmp_std_ut.dat", loader = FlatFileLoader.class)
  public void kmp_STD_UT_prefix(Scenario_Failure_Tbl c) {
    captureTestCase(c);

    KMP_Standard alg = new KMP_Standard();
    int[] tbl =
        (int[]) InvokePrivateHelper.invokePrivate(alg, "kmpTable", c.t());
    assertArrayEquals(c.expected(), tbl);
  }

  public record Scenario_KMP(@ParseWith(DataType.STRING) String s,
      @ParseWith(DataType.STRING) String t,
      @ParseWith(DataType.LIST_INTEGER) List<Integer> expected)
      implements Scenario {
  }

  @FileData(path = "kmp.dat", loader = FlatFileLoader.class)
  public void kmp_std_test(Scenario_KMP c) {
    captureTestCase(c);
    KMP alg = new KMP_Standard();
    assertEquals(c.expected(), alg.kmp(c.s(), c.t()),
        () -> "s=[" + c.s() + "] t=[" + c.t() + "]");
  }

  @FileData(path = "kmp.dat", loader = FlatFileLoader.class)
  public void kmp_test_me2(Scenario_KMP c) {
    captureTestCase(c);
    KMP alg = new KMP_Me2();
    assertEquals(c.expected(), alg.kmp(c.s(), c.t()),
        () -> "s=[" + c.s() + "] t=[" + c.t() + "]");
  }

  public record Scenario_KMP_Single(@ParseWith(DataType.STRING) String s,
      @ParseWith(DataType.STRING) String t,
      @ParseWith(DataType.INTEGER) Integer expected) implements Scenario {
  }

  @FileData(path = "kmp.single.dat", loader = FlatFileLoader.class)
  public void kmp_single_test(Scenario_KMP_Single c) {
    KMP_Single2 ks = new KMP_Single2();
    assertEquals(c.expected(), ks.kmp(c.s(), c.t()));
  }


  @FileData(path = "kmp_perf.dat", loader = FlatFileLoader.class)
  public void kmp_perf_test_KMP_Standard(Scenario_KMP c) {
    captureTestCase(c);

    KMP_Standard alg = new KMP_Standard();
    assertEquals(c.expected(), alg.kmp(c.s(), c.t()));
  }

  @FileData(path = "kmp_perf.dat", loader = FlatFileLoader.class)
  public void kmp_perf_test_KMP_Me2(Scenario_KMP c) {
    captureTestCase(c);

    KMP_Me2 alg = new KMP_Me2();
    assertEquals(c.expected(), alg.kmp(c.s(), c.t()));
  }

}
