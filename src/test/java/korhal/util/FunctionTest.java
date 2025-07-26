package korhal.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

/**
 * Conceptually, lambdas can be thought of as "syntax sugar" for functional
 * interface implementations — and practically that's true — but technically
 * it's a more optimized and bytecode-level feature, not just rewriting behind
 * the scenes.
 *
 * Java lambdas are actually a language + bytecode feature introduced in
 * Java 8 via {@code invokedynamic}.
 *
 * Semantically, lambdas is similar to anonymous functions, they are not
 * anonymous inner classes and have different runtime behavior.
 * - Lambdas use invokedynamic and are compiled more efficiently.
 * - They don’t create additional class files like anonymous classes do.
 * - They capture variables more efficiently (only what’s needed, lazily).
 *
 * Consumer<T>, Executable, Runnable, Callable<T>, Function<T, T>,
 * Predicate<T>, Supplier<T>.
 *
 * | Functional Interface | Method Signature | Matching Lambda Example |
 * | --------------------------- | --------------------------------- |
 * ------------------------------------- |
 * | `Consumer<String>` | `void accept(String s)` | `s -> System.out.println(s)`
 * |
 * | `Executable` (JUnit 5) | `void execute() throws Throwable` | `() ->
 * doSomethingThatMayThrow()` |
 * | `Runnable` | `void run()` | `() -> System.out.println("running")` |
 * | `Callable<Integer>` | `Integer call() throws Exception` | `() -> 42` |
 * | `Function<String, Integer>` | `Integer apply(String s)` | `s -> s.length()`
 * |
 * | `Predicate<String>` | `boolean test(String s)` | `s -> s.isEmpty()` |
 * | `Supplier<String>` | `String get()` | `() -> "hello"` |
 */

public class FunctionTest {

  /**
   * NOTE:
   * ```java
   * default void forEach(Consumer<? super T> action) {
   * Objects.requireNonNull(action);
   * for (T t : this) {
   * action.accept(t);
   * }
   * }
   */
  @Test
  public void consumer_test_1() {
    List<String> l = List.of("Messi", "Crespo", "Alvarez");
    StringBuilder sb = new StringBuilder();
    l.forEach((s) -> sb.append(s)); // forEach(Consumer<? super T> action)
    assertEquals("MessiCrespoAlvarez", sb.toString());
  }


  @Test
  public void cusumer_test_2() {
    Consumer<String> describe = (s) -> {
      switch (s) {
        case "Pele":
        case "Maradona":
          System.out.println(s + " is a former GOAT.");
          break;
        case "Messi":
          System.out.println(s + " is the GOAT.");
          break;
        default:
          System.out.println(s + " is irrelevant to the GOAT topic.");
          break;
      }
    };
    describe.accept("Messi");
    describe.accept("Pele");
    describe.accept("Maradona");
    describe.accept("Ronaldo");
  }

  @Test
  public void predicate_test() {
    Predicate<String> goat = (s) -> "Messi".equals(s);
    assertTrue(goat.test("Messi"));
    assertFalse(goat.test("Maradona"));
    assertFalse(goat.test("Pele"));
  }

  private Supplier<Integer> makeAdder(int x) {
    return () -> x + 1;  // x is captured
  }

  @Test
  public void supplier_test() {
    Supplier<Integer> addOne = makeAdder(10);
    assertEquals(11, addOne.get());
    assertEquals(11, addOne.get());

    addOne = makeAdder(-1);
    assertEquals(0, addOne.get());
    assertEquals(0, addOne.get());
  }

  @Test
  public void closure_test() {
    int c = 100;
    // c += 200;    // Error: required to be final or effective final
    Runnable task = () -> {
      int a = 5;
      int b = a + 5;
      // int c = 200; // Error: shadowing captured variable c
      a++;
      b++;
      System.out.println("a = " + a + " b = " + b);
      a += c;
      b += c;
      System.out.println("a = " + a + " b = " + b);

      // c++;       // Error: required to be final or effective final
    };
    task.run();     // a = 6 b = 11, a = 106, b = 111
    task.run();     // a = 6 b = 11, a = 106, b = 111
    // c += 200;    // Error: required to be final or effective final
    task.run();     // a = 6 b = 11, a = 106, b = 111
  }
}

