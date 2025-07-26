package korhal.sun.misc;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;
import sun.misc.Unsafe;

@Getter @Setter
class ChangeThread implements Runnable {
  // volatile boolean flag = false;     // this works w/o Unsafe.loadFence()
  /* volatile */ boolean flag = false;  // Unsafe.loadFence() needed to work

  @Override
  public void run() {
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("subThread change flag to:" + flag);
    flag = true;
  }
}

public class UnsafeTest2 {
  public static void main(String[] args) throws Exception {
    System.out.println("Hello Unsafe!");

    Field f = Unsafe.class.getDeclaredField("theUnsafe");
    f.setAccessible(true);
    Unsafe u = (Unsafe) f.get(null);

    ChangeThread changeThread = new ChangeThread();
    new Thread(changeThread).start();
    while (true) {
      boolean flag = changeThread.isFlag();
      u.loadFence(); // if no fence, the main thread will loop forever when
                     // the flag is not declared as **volatile**
      if (flag) {
        System.out.println("detected flag changed");
        break;
      }
    }
    System.out.println("main thread end");
  }
}
