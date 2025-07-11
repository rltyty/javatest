package korhal.lang;

public class ClassTest {
  int a;
  int b = 5;

  public ClassTest(int arg0) {
    a = arg0;
  }

  // As long as users overload constructor method, no default initialization
  // will be provided. Then users should explicitly provide a default
  // constructor (no arguments) by themselves.
  public ClassTest() {}

  public String toString() {
    return new StringBuffer().append(a).append(b).toString();
  }

  public static void main(String[] args) {
    ClassTest ct;
    if (args.length > 0) {
      ct = new ClassTest(Integer.valueOf(args[0]));
      System.out.println(ct);
    } else {
      ct = new ClassTest(); // Error, otherwise
      System.out.println("Usage: 1 argument required. Default ClassTest:" + ct);
    }
  }
}
