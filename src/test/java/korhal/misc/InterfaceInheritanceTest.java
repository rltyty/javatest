package korhal.misc;

// Super Interface 1 with a default method and a field
interface SuperInterface1 {
    // Initialized when SuperInterface1 is loaded into the JVM
    long START_TIME_MILLIS = System.currentTimeMillis();; // Implicitly public static final

    default void defaultMethod1() {
        System.out.println("Default method from SuperInterface1");
    }
}

// Super Interface 2 with a default method and another field
interface SuperInterface2 {
    String MESSAGE = "Hello from SuperInterface2"; // Implicitly public static final

    default void defaultMethod2() {
        System.out.println("Default method from SuperInterface2");
    }
}

// Derived Interface extending both SuperInterface1 and SuperInterface2
interface DerivedInterface extends SuperInterface1, SuperInterface2 {
    void abstractMethod(); // An abstract method specific to DerivedInterface
}

// A class implementing the DerivedInterface
class MyClass implements DerivedInterface {
    @Override
    public void abstractMethod() {
        System.out.println("Implementing abstractMethod in MyClass");
    }
    // No need to implement defaultMethod1 or defaultMethod2, they are inherited
}

public class InterfaceInheritanceTest {
    public static void main(String[] args) {
        MyClass obj = new MyClass();

        // Calling inherited default methods
        obj.defaultMethod1(); // Inherited from SuperInterface1
        obj.defaultMethod2(); // Inherited from SuperInterface2

        // Calling its own abstract method implementation
        obj.abstractMethod();

        // Accessing inherited fields (constants)
        System.out.println("Constant from SuperInterface1: " + DerivedInterface.START_TIME_MILLIS);
        System.out.println("Constant from SuperInterface2: " + DerivedInterface.MESSAGE);
    }
}

