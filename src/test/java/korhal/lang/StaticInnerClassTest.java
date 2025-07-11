package korhal.lang;

import org.junit.jupiter.api.Test;

class Outer {
  public static class SInner {
    public void f() {
      System.out.println("SInner.f()");
    }
  }

  public class Inner {
    public void f() {
      System.out.println("Inner.f()");
    }
  }
}

public class StaticInnerClassTest {
  @Test
  public void test_static_inner(){
    Outer.SInner staticInner = new Outer.SInner();
    staticInner.f();
  }

  @Test
  public void test_inner() {
    Outer.Inner inner = new Outer().new Inner();
    inner.f();
  }
}
