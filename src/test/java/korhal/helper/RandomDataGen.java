package korhal.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.base.Strings;

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

  public static void main(String[] args) {
    int n = 0;
    int min = 0;
    int max = n;
    if (args.length > 0) {
      n = Integer.valueOf(args[0]);
      if (args.length > 1) {
        min = Integer.valueOf(args[1]);
        if (args.length > 2) {
          max = Integer.valueOf(args[2]);
        }
      }
    }
    System.out.println(getListOfInteger(n, min, max));
  }
}
