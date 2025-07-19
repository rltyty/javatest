package korhal.misc;

import org.junit.jupiter.api.Test;

public class NestedInterfaceTest {
  private interface InterfaceInClass {
    public interface InnerNestedInterfaceTest {
      static float AA = 5.5f;

      static void sf() {
        System.out.println("I'm InterfaceInClass.InnerNestedInterfaceTest.sf()");
      }

      public void innerf();
    }

    static int A = 5;

    static void sf() {
      System.out.println("I'm InterfaceInClass.sf()");
    }

    public void f();
  }

  private interface SubInterface extends InterfaceInClass {
    static int B = 10;
  }

  private class ClassImplInterfaceInClass implements InterfaceInClass {
    public void f() {
      System.out.println("Impl f()");
    }
  }

  @Test
  public void nestedinterfacetest() {
    ClassImplInterfaceInClass o = new ClassImplInterfaceInClass();
    o.f();

    // Demonstrate static members
    System.out.println("A = " + InterfaceInClass.A);
    InterfaceInClass.sf();

    System.out.println("AA = " + InterfaceInClass.InnerNestedInterfaceTest.AA);
    InterfaceInClass.InnerNestedInterfaceTest.sf();

    // Demonstrate nested interface usage
    class InnerImpl implements InterfaceInClass.InnerNestedInterfaceTest {
      public void innerf() {
        System.out.println("Implemented innerf()");
      }
    }
    InnerImpl inner = new InnerImpl();
    inner.innerf();
  }
}
