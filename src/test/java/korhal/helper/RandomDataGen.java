package korhal.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomDataGen {
  public static int[] getArrayOfInt(int size, int min, int maxExcl) {
    Random random = new Random();
    int[] arr = new int[size];
    for (int i = 0; i < size; i++) {
      arr[i] = random.nextInt(min, maxExcl);
    }
    return arr;
  }

  public static List<Integer> getListOfInteger(int size, int min, int maxExcl) {
    Random random = new Random();
    List<Integer> list = new ArrayList<>(size);
    for (int i = 0; i < size; i++) {
      int num = random.nextInt(min, maxExcl);
      list.add(num);
    }
    return list;
  }
}
