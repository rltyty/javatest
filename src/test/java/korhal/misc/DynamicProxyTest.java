package korhal.misc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.junit.jupiter.params.provider.ValueSource;

interface Combinatorics {
  long factorial(int n);
}

interface NumberTheory {
  long fibonacci_recur_stupid(int n);
  long fibonacci_iter(int n);
}

class Calculator implements Combinatorics, NumberTheory {
  @Override
  public long factorial(int n) {
    if (n < 0) return -1L;
    if (n == 0) return 1L;
    long result = 1L;
    for (int i = 2; i <= n; i++) result *= i;
    return result;
  }

  @Override
  public long fibonacci_recur_stupid(int n) {
    if (n < 0) return -1L;
    if (n <= 1) return (long) n;
    return fibonacci_recur_stupid(n-1) + fibonacci_recur_stupid(n-2);
  }

  @Override
  public long fibonacci_iter(int n) {
    if (n < 0) return -1L;
    if (n <= 1) return (long) n;
    long p = 1L, q = 0L, result = 0L;
    for (int i = 2; i <= n; i++) {
      result = p + q;
      q = p;
      p = result;
    }
    return result;
  }
}

class StopwatchInvocationHandler implements InvocationHandler {
  private Object target;
  public StopwatchInvocationHandler(Object target) {
    this.target = target;
  }
  public Object invoke(Object proxy, Method method, Object[] args) {
    Object result = null;
    try {
      long start = System.currentTimeMillis();
      result = method.invoke(target, args);
      long end = System.currentTimeMillis();
      System.out.printf("Time consumed: %d(ms)\n", end - start);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    return result;
  }
}

class DynamicProxyFactory {
  public static Object getStopwatchProxy(Object target) {
    InvocationHandler swih = new StopwatchInvocationHandler(target);
    return Proxy.newProxyInstance(
        target.getClass().getClassLoader(),
        target.getClass().getInterfaces(),
        swih);
  }
}

public class DynamicProxyTest {
  public static void main(String[] args) {
    int n = 5;
    if (args.length > 0) n = Integer.valueOf(args[0]);
    System.out.println("DynamicProxyTest:");
    System.out.println("1: Direct call:");

    Calculator c = new Calculator();

    System.out.println(c.factorial(n));
    System.out.println("2: Use dynamic proxy:");
    Combinatorics p = (Combinatorics)DynamicProxyFactory.getStopwatchProxy(c);
    System.out.printf("2.1: factorial(%d)=%d\n", n, p.factorial(n));
    NumberTheory f = (NumberTheory) DynamicProxyFactory.getStopwatchProxy(c);
    System.out.println("Stupid algorithm:");
    System.out.printf("2.2: fibonacci(%d)=%d\n", n, f.fibonacci_recur_stupid(n));
    System.out.println("Clever algorithm:");
    System.out.printf("2.3: fibonacci(%d)=%d\n", n, f.fibonacci_iter(n));

    InvocationHandler swih = new StopwatchInvocationHandler(c);

    Combinatorics cproxy = (Combinatorics) Proxy.newProxyInstance(
        Calculator.class.getClassLoader(),
        new Class<?>[]{Combinatorics.class},
        swih);
    System.out.printf("3.1: factorial(%d)=%d\n", n, cproxy.factorial(n));

    NumberTheory nproxy = (NumberTheory) Proxy.newProxyInstance(
        Calculator.class.getClassLoader(),
        new Class<?>[]{NumberTheory.class},
        swih);
    System.out.printf("3.2: fibonacci_iter(%d)=%d\n", n, nproxy.fibonacci_iter(n));

    nproxy = (NumberTheory) Proxy.newProxyInstance(
        Calculator.class.getClassLoader(),
        c.getClass().getInterfaces(),
        swih);
    System.out.printf("3.3: fibonacci_recur_stupid(%d)=%d\n", n, nproxy.fibonacci_recur_stupid(n));

  }
}
