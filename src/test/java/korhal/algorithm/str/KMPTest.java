package korhal.algorithm.str;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Timeout;
import korhal.helper.BaseTest;
import korhal.helper.annotation.FileData;
import korhal.helper.annotation.ParseWith;
import korhal.helper.loader.LineParserLoader;
import korhal.helper.parser.DataType;
import korhal.helper.parser.TestInput;
import korhal.helper.reflect.InvokePrivateHelper;

@Timeout(value = 1, unit = TimeUnit.SECONDS)
public class KMPTest extends BaseTest {

  public record Input_Prefix(@ParseWith(DataType.STRING) String t,
      @ParseWith(DataType.ARRAY_INTEGER) int[] expected) implements TestInput {
  }

  @FileData(type = Input_Prefix.class, path = "kmp_std_ut.dat",
      loader = LineParserLoader.class)
  public void kmp_UT_failure_table(Input_Prefix I) {
    captureInput(I);

    KMP alg = new KMP_Me2();
    int[] fb = (int[]) InvokePrivateHelper.invokePrivate(alg, "prefix", I.t());
    assertArrayEquals(I.expected(), fb);
  }

  @FileData(type = Input_Prefix.class, path = "kmp_std_ut.dat",
      loader = LineParserLoader.class)
  public void kmp_STD_UT_prefix(Input_Prefix I) {
    captureInput(I);

    KMP_Standard alg = new KMP_Standard();
    int[] tbl =
        (int[]) InvokePrivateHelper.invokePrivate(alg, "kmpTable", I.t());
    assertArrayEquals(I.expected(), tbl);
  }

  public record Input(@ParseWith(DataType.STRING) String s,
      @ParseWith(DataType.STRING) String t,
      @ParseWith(DataType.LIST_INTEGER) List<Integer> expected)
      implements TestInput {
  }

  @FileData(type = Input.class, path = "kmp.dat",
      loader = LineParserLoader.class)
  public void kmp_std_test(Input I) {
    captureInput(I);
    KMP alg = new KMP_Standard();
    assertEquals(I.expected(), alg.kmp(I.s(), I.t()),
        () -> "s=[" + I.s() + "] t=[" + I.t() + "]");
  }

  @FileData(type = Input.class, path = "kmp.dat",
      loader = LineParserLoader.class)
  public void kmp_test_me2(Input I) {
    captureInput(I);
    KMP alg = new KMP_Me2();
    assertEquals(I.expected(), alg.kmp(I.s(), I.t()),
        () -> "s=[" + I.s() + "] t=[" + I.t() + "]");
  }

  public record Input_Single(@ParseWith(DataType.STRING) String s,
      @ParseWith(DataType.STRING) String t,
      @ParseWith(DataType.INTEGER) Integer expected) implements TestInput {
  }

  @FileData(type = Input_Single.class, path = "kmp.single.dat",
      loader = LineParserLoader.class)
  public void kmp_single_test(Input_Single I) {
    KMP_Single2 ks = new KMP_Single2();
    assertEquals(I.expected(), ks.kmp(I.s(), I.t()));
  }


  @FileData(type = Input.class, path = "kmp_perf.dat",
      loader = LineParserLoader.class)
  public void kmp_perf_test_KMP_Standard(Input I) {
    captureInput(I);

    KMP_Standard alg = new KMP_Standard();
    assertEquals(I.expected(), alg.kmp(I.s(), I.t()));
  }

  @FileData(type = Input.class, path = "kmp_perf.dat",
      loader = LineParserLoader.class)
  public void kmp_perf_test_KMP_Me2(Input I) {
    captureInput(I);

    KMP_Me2 alg = new KMP_Me2();
    assertEquals(I.expected(), alg.kmp(I.s(), I.t()));
  }

}
