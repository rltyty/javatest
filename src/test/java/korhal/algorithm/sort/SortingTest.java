package korhal.algorithm.sort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Timeout;
import io.github.rltyty.jtkit.junit.helper.BaseTest;
import io.github.rltyty.jtkit.junit.helper.annotation.FileData;
import io.github.rltyty.jtkit.junit.helper.annotation.ParseWith;
import io.github.rltyty.jtkit.junit.helper.loader.FlatFileLoader;
import io.github.rltyty.jtkit.junit.helper.parser.DataType;
import io.github.rltyty.jtkit.junit.helper.parser.Scenario;

@Timeout(value = 2, unit = TimeUnit.SECONDS)
public class SortingTest extends BaseTest {

  public record SortData(
      @ParseWith(DataType.LIST_INTEGER) List<Integer> input,
      @ParseWith(DataType.BOOLEAN) boolean reverse) implements Scenario {
  }

  @BeforeAll
  public static void setUpAll() { // Warm the cache
      new FlatFileLoader<>(SortData.class)
      .load("small.dat", SortingTest.class);
      new FlatFileLoader<>(SortData.class)
      .load("large.dat", SortingTest.class);
  }

  @FileData(path = "small.dat", loader = FlatFileLoader.class)
  public void test_bubble_sort(SortData data) {
    captureTestCase(data);
    Comparator<Integer> cmp =
        data.reverse() ? Comparator.reverseOrder() : Comparator.naturalOrder();
    List<Integer> expected = new ArrayList<>(data.input());
    expected.sort(cmp);
    Sorting.bubbleSort(data.input(), cmp);
    assertEquals(expected, data.input());
  }

  @FileData(path = "small.dat", loader = FlatFileLoader.class)
  public void test_insertion_sort(SortData data) {
    captureTestCase(data);
    Comparator<Integer> cmp =
        data.reverse() ? Comparator.reverseOrder() : Comparator.naturalOrder();
    List<Integer> expected = new ArrayList<>(data.input());
    expected.sort(cmp);
    Sorting.insertionSort(data.input(), cmp);
    assertEquals(expected, data.input());
  }

  @FileData(path = "large.dat", loader = FlatFileLoader.class)
  public void test_baseline(SortData data) {
    captureTestCase(data);
    Comparator<Integer> cmp =
        data.reverse() ? Comparator.reverseOrder() : Comparator.naturalOrder();
    data.input().sort(cmp);
  }

  @FileData(path = "large.dat", loader = FlatFileLoader.class)
  public void test_quicksort(SortData data) {
    // captureTestCase(data); // too long to print
    Comparator<Integer> cmp =
        data.reverse() ? Comparator.reverseOrder() : Comparator.naturalOrder();
    List<Integer> expected = new ArrayList<>(data.input());
    expected.sort(cmp);
    Sorting.quickSort(data.input(), cmp);
    assertEquals(expected, data.input());
  }

}
