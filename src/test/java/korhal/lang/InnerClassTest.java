package korhal.lang;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Outer {
  public int m1(int i) {
    return i + 1;
  }

  private int m2(int i) {
    return i + 2;
  }

  protected int m3(int i) {
    return i + 3;
  }

  private static int m4(int i) {
    return i + 4;
  }

  private class NormalInner {
    public int n1(int i) {
      return m1(i) + 1000;
    }

    public static int n4(int i) {
      // return m1(i) + 1000;   // Error: non-static have no access to static
      return m4(i) + 1000;      // OK: static have access to static
    }
  }

  private class PrivateInner extends Outer {
    public int n1(int i) {
      return m1(i) + 10;
    }

    private int n2(int i) {
      return m2(i) + 10;        // OK: non-static inner can access outer
                                // private
    }

    protected int n3(int i) {
      return m3(i) + 10;
    }
  }

  private static class PrivateStaticInner extends Outer {
    public int n1(int i) {
      return m1(i) + 100;     // the implicit caller **this** is instance of
                              // inner, not an instance of outer.
                              // this.m1(i)
    }

    private int n2(int i) {
      // return m2(i) + 100;  // compilation error: static inner also as sub
                              // outer cannot access to outer private
                              // non-static for invisibility from both
                              // inheritance's and static/non-static's perspective.
      return m3(i) + 100;     // OK: sub outer inherits outer public
                              // this.m3(i)
    }

    private int n4(int i) {
      return m4(i) + 100;     // OK: static inner have access private static
                              // outer member, Outer.m4(i)
    }

    protected int n3(int i) {
      return m3(i) + 100;
    }
  }

  public int t1(int i) {
    PrivateInner pi = new PrivateInner();
    return pi.n1(i);
  }


  public int t2(int i) {
    PrivateStaticInner psi = new PrivateStaticInner();
    return psi.n1(i);
  }

}

class SubOuter extends Outer {
  public int n1(int i) {
    return m1(i) + 1000;
  }

  public int n2(int i) {
    // return m2(i) + 1000; // compilation error: super private members
    // return m4(i) + 1000; // no matter non-static or static are invisible
                            // to sub
    return m3(i);           // OK: outer protected visible to sub outer
  }
}

public class InnerClassTest {

  @Test
  public void test_inner_static() {
    Outer o = new Outer();
    assertEquals(1, o.m1(0));
    assertEquals(11, o.t1(0));
    assertEquals(101, o.t2(0));
  }

}
