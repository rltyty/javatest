package korhal.algorithm.sort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Timeout;
import io.github.rltyty.jtkit.junit.helper.BaseTest;
import io.github.rltyty.jtkit.junit.helper.annotation.FileData;
import io.github.rltyty.jtkit.junit.helper.annotation.ParseWith;
import io.github.rltyty.jtkit.junit.helper.loader.FlatFileLoader;
import io.github.rltyty.jtkit.junit.helper.parser.DataType;
import io.github.rltyty.jtkit.junit.helper.parser.Scenario;

@Timeout(value = 1, unit = TimeUnit.SECONDS)
public class InsertionSortTest extends BaseTest {

  public record SortData(
    @ParseWith(DataType.LIST_INTEGER) List<Integer> input,
    @ParseWith(DataType.BOOLEAN)      Boolean reverse, 
    @ParseWith(DataType.LIST_INTEGER) List<Integer> expected
    ) implements Scenario{
  }

  @FileData(path = "1.dat", loader = FlatFileLoader.class)
  public void test_insertion_sort(SortData data) {
    List<Integer> copy = new ArrayList<>(data.input());
    InsertionSort.sort(copy, data.reverse());
    assertEquals(data.expected(), copy);
  }

}

