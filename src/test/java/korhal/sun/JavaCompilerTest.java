package korhal.sun;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// com.sun.* packages, for internal use, not for developer
// use javax.tools.JavaCompiler
// import com.sun.tools.javac.main.JavaCompiler;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class JavaCompilerTest {
  public static void main(String[] args) {
    // Get the system's Java compiler
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    if (compiler == null) {
      System.err.println("No system Java compiler available. Ensure you are running with a JDK, not a JRE.");
      return;
    }

    // Define the source files to compile
    String[] filesToCompile = new String[] { "./src/test/java/korhal/sun/JavaCompilerTest.java" };

    // Run the compilation task
    // The arguments are the same as you would pass to the javac command line tool.
    // For example: -d <output_directory>
    List<String> argv = new ArrayList<>();
    argv.add("-d");
    argv.add("./");
    argv.addAll(Arrays.asList(filesToCompile));
    int compilationResult = compiler.run(null, null, null, argv.toArray(new String[0]));

    if (compilationResult == 0) {
      System.out.println("✅ Compilation was successful!");
    } else {
      System.out.println("❌ Compilation failed.");
    }
  }
}
