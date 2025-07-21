package korhal.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class FunctionTest {
  /**
   * Lambda (or anonymous function) in Java can be an implementation of
   * Consumer<T>, Executable, Runnable, Callable<T>, Function<T, T>,
   * Predicate<T>, Supplier<T>.
   */
  @Test
  public void consumer_test() {
    List<String> l = List.of("Messi", "Crespo", "Alvarez");
    StringBuilder sb = new StringBuilder();
    l.forEach((s) -> sb.append(s)); // forEach(Consumer<? super T> action)
    assertEquals("MessiCrespoAlvarez", sb.toString());
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
| Functional Interface        | Method Signature                  | Matching Lambda Example               |
| --------------------------- | --------------------------------- | ------------------------------------- |
| `Consumer<String>`          | `void accept(String s)`           | `s -> System.out.println(s)`          |
| `Executable` (JUnit 5)      | `void execute() throws Throwable` | `() -> doSomethingThatMayThrow()`     |
| `Runnable`                  | `void run()`                      | `() -> System.out.println("running")` |
| `Callable<Integer>`         | `Integer call() throws Exception` | `() -> 42`                            |
| `Function<String, Integer>` | `Integer apply(String s)`         | `s -> s.length()`                     |
| `Predicate<String>`         | `boolean test(String s)`          | `s -> s.isEmpty()`                    |
| `Supplier<String>`          | `String get()`                    | `() -> "hello"`                       |

 *
 */
