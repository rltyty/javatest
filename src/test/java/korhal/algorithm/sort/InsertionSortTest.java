package korhal.algorithm.sort;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Timeout;
import io.github.rltyty.jtkit.junit.helper.BaseTest;
import io.github.rltyty.jtkit.junit.helper.annotation.ParseWith;
import io.github.rltyty.jtkit.junit.helper.parser.DataType;
import io.github.rltyty.jtkit.junit.helper.parser.TestInput;

@Timeout(value = 1, unit = TimeUnit.SECONDS)
public class InsertionSortTest extends BaseTest {
  public record Input (
    @ParseWith(DataType.LIST_INTEGER) List<Integer> input,
    @ParseWith(DataType.LIST_INTEGER) List<Integer> output
  ) implements TestInput {
  }

}

