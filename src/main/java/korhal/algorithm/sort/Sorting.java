package korhal.algorithm.sort;

import java.util.Comparator;
import java.util.List;

public final class Sorting {

  private static <T extends Comparable <? super T>>
  void swap (List<T> arr, int i, int j) {
    T tmp = arr.get(i);
    arr.set(i, arr.get(j));
    arr.set(j, tmp);
  }

  /*
   * Sort list in place using bubble sort.
   *
   * Repeatedly compares adjacent elements and swaps them when they
   * are in the wrong order. After each pass, the largest remaining
   * element is moved to the end of the unsorted portion.
   *
   * Time complexity:
   *   Best:    O(n)    when the list is already sorted
   *   Average: O(n^2)
   *   Worst:   O(n^2)
   *
   * Space complexity: O(1)
   *
   * Bubble sort is stable.
   */
  public static <T extends Comparable <? super T>>
  void bubbleSort(List<T> arr, Comparator<? super T> cmp) {
    for (int i = arr.size() - 1; i > 0; i--) {
      boolean swapped = false;
      for (int j = 0; j < i; j++) {
        if (cmp.compare(arr.get(j+1), arr.get(j)) < 0) {
          swap(arr, j, j+1);
          swapped = true;
        }
      }
      if (!swapped) break;
    }
  }

  /*
   * Sort list in place using insertion sort.
   *
   * Maintains a sorted prefix of the list. For each new element,
   * shifts larger elements in the sorted prefix one position to the
   * right and inserts the element into its correct position.
   *
   * Time complexity:
   *   Best:    O(n)    when the list is already sorted
   *   Average: O(n^2)
   *   Worst:   O(n^2)
   *
   * Space complexity: O(1)
   *
   * Insertion sort is stable.
   */
  public static <T extends Comparable <? super T>>
  void insertionSort(List<T> arr, Comparator<? super T> cmp) {
    for (int i = 1; i < arr.size(); i++) {
      T t = arr.get(i);
      int j = i - 1;
      while (j >= 0 && cmp.compare(t, arr.get(j)) < 0)  {
        arr.set(j+1, arr.get(j));
        j--;
      }
      arr.set(j + 1, t);
    }
  }

  /*
   * Sort list in place using quicksort.
   *
   * Uses the first element as the pivot and partitions the list into
   * two parts:
   *   - elements smaller than the pivot
   *   - elements greater than or equal to the pivot
   *
   * The pivot is then placed between these two partitions, and the
   * same process is recursively applied to both partitions.
   *
   * Time complexity:
   *   Average: O(n log n)
   *   Worst:   O(n^2)  when partitions are highly unbalanced
   *
   * Space complexity:
   *   Average: O(log n) recursion stack
   *   Worst:   O(n)
   *
   * This implementation is not stable.
   */
  public static <T extends Comparable <? super T>>
  void quickSort(List<T> arr, Comparator<? super T> cmp) {
    if (arr.size() == 0) return;
    T pivot = arr.get(0);
    int cur = 1;
    int i = 0, j = arr.size() - 1;
    while (i < j) {
      if (cmp.compare(arr.get(cur), pivot) < 0) {
        arr.set(i, arr.get(cur));
        i++;
        cur++;
      } else {
        swap(arr, cur, j);
        j--;
      }
    }
    arr.set(i, pivot);
    quickSort(arr.subList(0, i), cmp);
    quickSort(arr.subList(i+1, arr.size()), cmp);
  }

  public static <T extends Comparable <? super T>>
  void mergeSort(List<T> arr, Comparator<? super T> cmp) {
  }

  public static <T extends Comparable <? super T>>
  void heapSort(List<T> arr, Comparator<? super T> cmp) {
  }

  public static <T extends Comparable <? super T>>
  void timSort(List<T> arr, Comparator<? super T> cmp) {
  }

}
