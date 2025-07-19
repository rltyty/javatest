package korhal.misc;

import java.lang.reflect.Method;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.junit.jupiter.api.Test;

class YACalculator {
  public long factorial(int n) {
    if (n < 0) return -1L;
    if (n == 0) return 1L;
    long result = 1L;
    for (int i = 2; i <= n; i++) result *= i;
    return result;
  }

  public long fibonacci_recur_stupid(int n) {
    if (n < 0) return -1L;
    if (n <= 1) return (long) n;
    return fibonacci_recur_stupid(n - 1) + fibonacci_recur_stupid(n - 2);
  }

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

class StopwatchInterceptor implements MethodInterceptor {

  @Override
  public Object intercept(Object target, Method m, Object[] args, MethodProxy mp) throws Throwable {
    Object result = null;
    result = null;
    long start = System.currentTimeMillis();
    result = mp.invokeSuper(target, args);
    long end = System.currentTimeMillis();
    System.out.printf("Time consumed: %d(ms)\n", end - start);
    return result;
  }
}

class CGlibProxyFactory {
  public static Object getProxy(Class<?> clazz) {
    // 创建动态代理增强类
    Enhancer enhancer = new Enhancer();
    // 设置类加载器
    enhancer.setClassLoader(clazz.getClassLoader());
    // 设置被代理类
    enhancer.setSuperclass(clazz);
    // 设置方法拦截器
    enhancer.setCallback(new StopwatchInterceptor());
    // 创建代理类
    return enhancer.create();
  }
}

public class CGLibTest {
  public static int n = 5;

  public static void main(String[] args) {
    if (args.length > 0) CGLibTest.n = Integer.valueOf(args[0]);
  }

  @Test
  public void cglib_test() {
    YACalculator calProxy = (YACalculator) CGlibProxyFactory.getProxy(YACalculator.class);
    System.out.printf(
        "fibonacci_recur_stupid(%d)=%d\n",
        CGLibTest.n, calProxy.fibonacci_recur_stupid(CGLibTest.n));
    System.out.printf(
        "fibonacci_iter(%d)=%d\n",
        CGLibTest.n, calProxy.fibonacci_iter(CGLibTest.n));
  }
}
