package korhal.helper.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class Parser {

  /**
   * Function<T, R>
   *          ↑  ↑
   *      input  output
   * e.g. String valueOf
   * These are equivalent:
   * Function<LineParser, Object> fn = LineParser::getBoolean;
   * Function<LineParser, Object> fn = parser -> parser.getBoolean();
   */
  private static final Map<Class<?>, Function<String, Object>>
    LOW_PARSERS = Map.ofEntries(
        Map.entry(Boolean.class,    Boolean::valueOf),
        Map.entry(Byte.class,       Byte::valueOf),
        Map.entry(Short.class,      Short::valueOf),
        Map.entry(Integer.class,    Integer::valueOf),
        Map.entry(Long.class,       Long::valueOf),
        Map.entry(Float.class,      Float::valueOf),
        Map.entry(Double.class,     Double::valueOf),
        Map.entry(String.class,     Parser::stripQuotes),
        Map.entry(Character.class,  Parser::str2Char)
    );

  public static Map<DataType.Arity, BiFunction<Parser, Class<?>, Object>>
    HIGH_PARSERS = Map.of(
      DataType.Arity.SINGLE,    (parser, clazz) -> parser.getSingle(clazz),
      DataType.Arity.LIST,      (parser, clazz) -> parser.getList(clazz),
      DataType.Arity.LIST_2D,   (parser, clazz) -> parser.getList2D(clazz)
  );

  public abstract <T> T getSingle(Class<T> t);
  public abstract <T> List<T> getList(Class<T> t);
  public abstract <T> List<List<T>> getList2D(Class<T> t);

  /**
   * Extracts content inside outermost [ ].
   * Throws if the input is not a properly bracket-enclosed list.
   */
  private static String stripBrackets(String s)
      throws IllegalArgumentException {
    String stripped = s.strip();
    if (!stripped.startsWith("[") || !stripped.endsWith("]")) {
      throw new IllegalArgumentException("Expected bracket-closed list, "
        + "but got [" + stripped + "].");
    }
    return stripped.substring(1, stripped.length()-1).strip();
  }

  /**
   * Splits content, e.g. "a, b, [1, 2], c" at top-level commas only.
   */
  private static List<String> splitElements(String s) {
    List<String> elements = new ArrayList<>();
    int depth = 0;
    int start = 0;
    for (int i = start; i < s.length(); i++) {
      char c = s.charAt(i);
      if        (c == '[') depth++;
      else if   (c == ']') depth--;
      else if   (c == ',' && depth == 0) {
        elements.add(s.substring(start, i).strip());
        start = i + 1;
      }
    }
    String tail = s.substring(start).strip();
    if (!tail.isBlank()) elements.add(tail);
    return elements;
  }

  protected static <E> List<List<E>> getList2D(String s, Class<E> t) {
    try {
      String content = stripBrackets(s);
      if (content.isBlank()) return new ArrayList<>();

      List<List<E>> list2D = new ArrayList<>();
      for (String part : splitElements(content)) {
        List<E> inner = getList(part, t);
        if (inner == null) {
          return null;
        }
        list2D.add(inner);
      }
      return list2D;
    } catch (IllegalArgumentException iae) {
      System.err.println("Failed to parse [" + s + "] as a 2D-list of "
          + t.getSimpleName() + ": " + iae.getMessage());
      return null;
    }

  }

  protected static <E> List<E> getList(String s, Class<E> t) {
    try {
      String content = stripBrackets(s);
      if (content.isBlank()) return new ArrayList<>();

      List<E> list = new ArrayList<>();
      for (String part : splitElements(content)) {
        E e = getSingle(part, t);
        if (e == null) {
          return null;
        }
        list.add(e);
      }
      return list;
    } catch (IllegalArgumentException iae) {
      System.err.println("Failed to parse [" + s + "] as a list of "
          + t.getSimpleName() + ": " + iae.getMessage());
      return null;
    }
  }

  protected static <T> T getSingle(String s, Class<T> t) {
    Function<String, Object> func = LOW_PARSERS.get(t);
    if (func == null)  {
      System.err.println("Unsupported type: " + t.getName());
      return null;
    }
    T obj = null;
    try {
      obj = t.cast(func.apply(s));
    } catch (IllegalArgumentException iae) {
      System.err.println("Failed to parse [" + s + "] as "
          + t.getSimpleName() + ": " + iae.getMessage());
    }
    return obj;
  }

  private static Character str2Char(String s) {
    String trimmed = stripQuotes(s);
    if (trimmed.length() != 1) {
      System.err.println("Expected single character but got : ["
          + trimmed + "].");
    }
    return trimmed.charAt(0);
  }

  private static String stripQuotes(String s) throws IllegalArgumentException {
    if (s == null)
      return null;
    int len = s.length();
    if (len >= 2) {
      char first = s.charAt(0);
      char last = s.charAt(len - 1);

      if ((first == '"' && last == '"') ||
          (first == '\'' && last == '\'')) {
        return s.substring(1, len - 1);
      }
    }
    throw new IllegalArgumentException(
      "String value must be quoted but got: [" + s + "]"
    );
  }

}
