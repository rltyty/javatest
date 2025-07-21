package korhal.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Executable;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

/**
 * Lambda (or anonymous function) in Java can be an implementation of
 * Consumer<T>, Executable, Runnable, Callable<T>, Function<T, T>,
 * Predicate<T>, Supplier<T>.
| Functional Interface        | Method Signature                  | Matching Lambda Example               |
| --------------------------- | --------------------------------- | ------------------------------------- |
| `Consumer<String>`          | `void accept(String s)`           | `s -> System.out.println(s)`          |
| `Executable` (JUnit 5)      | `void execute() throws Throwable` | `() -> doSomethingThatMayThrow()`     |
| `Runnable`                  | `void run()`                      | `() -> System.out.println("running")` |
| `Callable<Integer>`         | `Integer call() throws Exception` | `() -> 42`                            |
| `Function<String, Integer>` | `Integer apply(String s)`         | `s -> s.length()`                     |
| `Predicate<String>`         | `boolean test(String s)`          | `s -> s.isEmpty()`                    |
| `Supplier<String>`          | `String get()`                    | `() -> "hello"`                       |
 */

public class FunctionTest {

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
      switch(s) {
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
}

/**
 * NOTE:
```java
   default void forEach(Consumer<? super T> action) {
     Objects.requireNonNull(action);
     for (T t : this) {
       action.accept(t);
     }
   }
```
 *
 */
