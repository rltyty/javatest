package korhal.misc;

// Super Interface 1 with a default method and a field
interface SuperInterface1 {
  // Initialized when SuperInterface1 is loaded into the JVM
  long START_TIME_MILLIS = System.currentTimeMillis();; // Implicitly public static final

  default void defaultMethod1() {
    System.out.println("Default method from SuperInterface1");
  }

  public void method_samename();

  // compilation error if duplicate methods among different super interfaces.
  // default void defaultMethod_samename() {
  //   System.out.println("defaultMethod_samename() from SuperInterface1");
  // }
}

// Super Interface 2 with a default method and another field
interface SuperInterface2 {
  String MESSAGE = "Hello from SuperInterface2"; // Implicitly public static final

  default void defaultMethod2() {
    System.out.println("Default method from SuperInterface2");
  }

  public void method_samename();

  // compilation error if duplicate methods among different super interfaces.
  // default void defaultMethod_samename() {
  //   System.out.println("defaultMethod_samename() from SuperInterface2");
  // }
}

// Derived Interface extending both SuperInterface1 and SuperInterface2
interface DerivedInterface extends SuperInterface1, SuperInterface2 {
  void abstractMethod(); // An abstract method specific to DerivedInterface
}

// Derived Interface extending both SuperInterface1 and SuperInterface2
interface DerivedInterface2 extends DerivedInterface {
}

// A class implementing the DerivedInterface
class MyClass implements DerivedInterface {
  @Override
  public void abstractMethod() {
    System.out.println("Implementing abstractMethod in MyClass");
  }
  // No need to implement defaultMethod1 or defaultMethod2, they are inherited


  @Override
  public void method_samename() {
    System.out.println("implementation abstract method, of which there are multiple same signature of it in super interfaces");
  }
}

class MyClassSub extends MyClass {
  public void method() {
    System.out.println("Access from Myclass2.method(): " + START_TIME_MILLIS);
    System.out.println("Access from Myclass2.method(): " + MESSAGE);
  }

}

public class InterfaceInheritanceTest {

  @SuppressWarnings("static")            // use this for clean output
  private void static_access_via_instatnce_test(MyClass obj) {
    // Accessing inherited fields (constants) using object
    // But access static fields via instance is not discouraged
    // System.out.println(obj.START_TIME_MILLIS);  // COMPILATION WARNING
    // System.out.println(obj.MESSAGE);            // COMPILATION WARNING
    System.out.println(MyClass.START_TIME_MILLIS);
    System.out.println(MyClass.MESSAGE);
  }

  public static void main(String[] args) {
    InterfaceInheritanceTest t = new InterfaceInheritanceTest();

    // Accessing inherited fields (constants) from direct derived interface
    System.out.println("Constant from SuperInterface1: " + DerivedInterface.START_TIME_MILLIS);
    System.out.println("Constant from SuperInterface2: " + DerivedInterface.MESSAGE);

    // Accessing inherited fields (constants) from indirect derived interface
    System.out.println("Constant from SuperInterface1: " + DerivedInterface2.START_TIME_MILLIS);
    System.out.println("Constant from SuperInterface2: " + DerivedInterface2.MESSAGE);

    // Accessing inherited fields (constants) from implementation class
    System.out.println("Constant from SuperInterface1: " + MyClass.START_TIME_MILLIS);
    System.out.println("Constant from SuperInterface2: " + MyClass.MESSAGE);

    // Accessing inherited fields (constants) from implementation class's derived class.
    System.out.println("Constant from SuperInterface1: " + MyClassSub.START_TIME_MILLIS);
    System.out.println("Constant from SuperInterface2: " + MyClassSub.MESSAGE);

    MyClass obj = new MyClass();

    // Calling inherited default methods
    obj.defaultMethod1(); // Inherited from SuperInterface1
    obj.defaultMethod2(); // Inherited from SuperInterface2

    // Calling its own abstract method implementation
    obj.abstractMethod();

    // Accessing inherited fields (constants) using object
    // But access static fields via instance is not discouraged
    t.static_access_via_instatnce_test(obj);

    MyClassSub obj2 = new MyClassSub();

    // Calling indirect inherited default methods
    obj2.defaultMethod1(); // Inherited from SuperInterface1
    obj2.defaultMethod2(); // Inherited from SuperInterface2

    // Calling its own abstract method implementation
    obj2.abstractMethod();
    obj2.method();

    // Accessing inherited fields (constants) using object
    // But access static fields via instance is not discouraged
    t.static_access_via_instatnce_test(obj2);

    obj2.method_samename();
  }
}
