package korhal.misc;

import org.junit.jupiter.api.Test;

interface MyInterface {

  // A private instance method
  private void commonHelperForDefault() {
    System.out.println("Executing common logic for default methods.");
  }

  // A default method calling the private instance method
  default void performAction() {
    System.out.println("Starting action...");
    commonHelperForDefault(); // Calls the private instance method
    System.out.println("Action finished.");
  }

  // A private static method
  private static String getSecretMessage() {
    return "This is a secret static message.";
  }

  // A static method calling the private static method
  static void printInfo() {
    System.out.println("Interface info: " + getSecretMessage()); // Calls the private static method
  }
}

interface MyInterface2 extends MyInterface {
  default void printInfo2() {
    System.out.println("Call MyInterface.printInfo() from MyInterface2");
    MyInterface.printInfo();
    // MyInterface2.printInfo();  // ERROR! compile time binding error
                                  // printInfo() belongs to MyInterface
    System.out.println("Leave MyInterface2.printInfo2()");
  }
}

class MyClass1 implements MyInterface {
  public static void staticMethodofMyClass1() {
    System.out.println("I'm staticMethodofMyClass1()");
  }
}

class MyClass2 extends MyClass1 {
  public static void staticMethodofMyClass2() {
    System.out.println("I'm staticMethodofMyClass2()");
  }
}

class MyClass3 extends MyClass2 {
  public static void staticMethodofMyClass3() {
    System.out.println("I'm staticMethodofClass3()");
  }
}

public class StaticPrivateTest {
  @Test
  public void test_interface() {
    MyClass1 o1 = new MyClass1();
    o1.performAction(); // Calls the default method, which uses the private helper
    MyInterface.printInfo(); // Calls the static method, which uses the private static helper
    MyClass1.staticMethodofMyClass1();
    MyClass2.staticMethodofMyClass2();
    MyClass3.staticMethodofMyClass3();

    MyClass2.staticMethodofMyClass1();
    MyClass3.staticMethodofMyClass2();  // This is OK seems to inherit but not
                                        // in the way of polymorphism
    MyClass3.staticMethodofMyClass1();

  }
}
