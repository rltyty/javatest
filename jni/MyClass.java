public class MyClass {
  static {
    System.loadLibrary("mylib"); // Load the shared library libmylib.so on Linux
                                 // libmylib.dylib on macOS
                                 // libmylib.dll on Windows
  }

  public native String myNativeMethod(String msg); // Declaration of native method

  public static void main(String[] args) { // java main args no executable name
    if (args.length > 0) {
      String res = new MyClass().myNativeMethod(args[0]); // Calls C function via JNI
      System.out.println("Received from native: <" + res + ">");
    }
  }
}
