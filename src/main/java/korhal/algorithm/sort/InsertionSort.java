package korhal.algorithm.sort;

import java.util.Comparator;
import java.util.List;

public class InsertionSort {

  /**
   * Sort list in place using insertion sort.
   *
   * @param arr List to sort. Elements must be Comparable.
   * @param reverse If true, sort in descending order. Defaults to ascending.
   */
  public static <T extends Comparable<T>> void sort(List<T> arr,
      boolean reverse) {
    Comparator<T> cmp =
        reverse ? Comparator.reverseOrder() : Comparator.naturalOrder();

    for (int i = 1; i < arr.size(); i++) {
      T key = arr.get(i);
      int j = i;

      while (j > 0 && cmp.compare(key, arr.get(j - 1)) < 0) {
        arr.set(j, arr.get(j - 1));
        j--;
      }

      arr.set(j, key);
    }
  }

  /** Convenience overload — ascending by default. */
  public static <T extends Comparable<T>> void sort(List<T> arr) {
    sort(arr, false);
  }
}

