package korhal.lang;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Arrays;
import java.util.Objects;

import org.junit.jupiter.api.Test;

public class CloneableTest {

  @Test
  public void cloneable_test() {
    B b = new B();
    // B bb = b.clone();            // Error. Due to the protected Object's
                                    // clone() is invisible from bb, the
                                    // instance of sub class of Object.
                                    // Need to override by sub-class
                                    // like class C.
                                    // Cloneable interface has no clone()
                                    // method defined. It is a marker
                                    // interface and signals to JVM and
                                    // checked at runtime, not compile time.

    String s = "Cxx";
    int[] a = new int[] {1, 2, 3};

    C c = new C(s, a);
    C d = c.clone();
    assertNotSame(c, d);            // different objects
    assertNotEquals(c, d);          // no overridden equals(), different objects
    assertTrue(c.getS() == d.getS());  // same object, immutable String
    assertNotSame(c.getA(), d.getA()); // different objects
    assertNotEquals(c.getA(), d.getA()); // False, int[].equals defaults to
                                         // Object's equals, just ref compare
    assertArrayEquals(c.getA(), d.getA());

    E e = new E(s, a);
    E f = e.clone();
    assertNotSame(e, f);
    assertEquals(e, f);             // equals() overridden, field value
                                    // comparison
    assertTrue(e.getS() == f.getS());
    assertNotSame(e.getA(), f.getA());
    assertNotEquals(e.getA(), f.getA());
    assertArrayEquals(e.getA(), f.getA());

    f.setS("Java");
    assertNotEquals(e, f);
    f.setS("Cxx");
    assertEquals(e, f);
    f.getA()[0] = 6;
    assertNotEquals(e, f);
    f.getA()[0] = 1;
    assertEquals(e, f);
  }
}

class A {
  private int a;
}

class B implements Cloneable {
  public void x() {
    try {
      Object o = super.clone();     // inaccessible from outside of the class
                                    // or the package of the Object
    } catch (CloneNotSupportedException cnse) {
    }
  }
}

class C implements Cloneable {
  private String s; // immutable, not Cloneable
  private int[] a;  // immutable, Cloneable

  public C() {}

  public C(String s, int[] a) {
    this.s = s;
    this.a = a;
  }

  @Override
  public C clone() {
    try {
      C c = (C) super.clone();
      c.setS(s);                    // no need to copy immutable String
      c.setA(a == null ? null :a.clone()); // clone: JVM built-in implementation
      return c;
    } catch (CloneNotSupportedException cnse) {
      throw new AssertionError();
    }
  }

  public int[] getA() {
    return a;
  }

  public void setA(int[] a) {
    this.a = a;
  }

  public String getS() {
    return s;
  }

  public void setS(String s) {
    this.s = s;
  }
}

class E implements Cloneable {
  private String s; // immutable, not Cloneable
  private int[] a;  // immutable, Cloneable

  public E() {}

  public E(String s, int[] a) {
    this.s = s;
    this.a = a;
  }

  @Override
  public E clone() {
    try {
      E e = (E) super.clone();
      e.setS(s);                    // no need to copy immutable String
      e.setA(a == null ? null :a.clone()); // clone: JVM built-in implementation
      return e;
    } catch (CloneNotSupportedException cnse) {
      throw new AssertionError();
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(s, Arrays.hashCode(a));
  }

  // @Override
  // public boolean equals(Object o) { // Override Object's equals(Object)
  //   if (this == o) return true;
  //   if (!(o instanceof E)) return false;
  //   E other = (E) o;
  //   return Objects.equals(s, other.s) && Arrays.equals(this.a, other.a);
  // }

  @Override
  public boolean equals(Object o) { // Override Object's equals(Object)
    if (this == o) return true;
    return (o instanceof E other)   // Java 17+, two lines --> one line
      && Objects.equals(s, other.s)
      && Arrays.equals(a, other.a);
  }

  public int[] getA() {
    return a;
  }

  public void setA(int[] a) {
    this.a = a;
  }

  public String getS() {
    return s;
  }

  public void setS(String s) {
    this.s = s;
  }
}

