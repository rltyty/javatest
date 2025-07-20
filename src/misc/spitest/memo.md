## SPI Test

### An IDE Language Server Protocol (LSP) Example

#### Service Protocol (Interface)

```java
package korhal.spi.lsp;

public interface LanguageService {
  public String getLSName();

  public String getType();

  public String[] diagnose(byte[] codes);
}
```

#### Package Interfaces

```sh
BUILD_ROOT=build.spi
MOD=proto
TARGET=spi-lsp.jar
javac -d "$BUILD_ROOT/$MOD" -sourcepath $MOD "$MOD"/korhal/spi/lsp/*.java # *.java works in no quotes
jar cf "$BUILD_ROOT/$MOD/$TARGET" -C "$BUILD_ROOT/$MOD" korhal
```

#### Service Provider (Implementation)

```java
package korhal.spi.lsp.impl.c;

import korhal.spi.lsp.LanguageService;

public class C_LS implements LanguageService {

  @Override
  public String getLSName() {
    return "C_LS";
  }

  @Override
  public String getType() {
    return "C";
  }

  @Override
  public String[] diagnose(byte[] codes) {
    return new String[] {
      "Ex1_1:10:5: error: use of undeclared identifier 'a'",
      "Ex1_1:25:9: warning: using the result of an assignment as a condition without parentheses [-Wparentheses]"
    };
  }

}
```
```java
package korhal.spi.lsp.impl.java;

import korhal.spi.lsp.LanguageService;

public class Java_LS implements LanguageService {

  @Override
  public String getLSName() {
    return "Java_LS";
  }

  @Override
  public String getType() {
    return "Java";
  }

  @Override
  public String[] diagnose(byte[] codes) {
    return new String[] {
      "StopwatchExtension.java:[41,2] Type mismatch: cannot convert from long to boolean [16777233]",
      "BaseTest.java:[85,9] The value of the local variable i is not used [536870973]",
    };
  }

}
```

`provider/META-INF/services/korhal.spi.lsp.LanguageService`

```
korhal.spi.lsp.impl.c.C_LS
korhal.spi.lsp.impl.java.Java_LS
```

#### Package Implementation Library

```sh
BUILD_ROOT=build.spi
MOD=provider
TARGET=spi-lsp-impl.jar
LIB_DIR=lib
LIB="spi-lsp.jar"

cp "$BUILD_ROOT/proto/$LIB" "$MOD/lib"
javac -cp "$MOD/$LIB_DIR/$LIB" -d "$BUILD_ROOT/$MOD" -sourcepath "$MOD" "$MOD"/korhal/spi/lsp/impl/c/*.java
javac -cp "$MOD/$LIB_DIR/$LIB" -d "$BUILD_ROOT/$MOD" -sourcepath "$MOD" "$MOD"/korhal/spi/lsp/impl/java/*.java
jar cf "$BUILD_ROOT/$MOD/$TARGET" -C "$BUILD_ROOT/$MOD" korhal -C "$MOD" META-INF
```

#### Build and Run Application (Client)

```java
package korhal.spi.lsp.client;

import korhal.spi.lsp.LanguageService;

import java.util.Arrays;
import java.util.ServiceLoader;

public class MyNeovim {
  public static void main(String[] args) {
    System.out.println("SPI Tests:");
    ServiceLoader<LanguageService> loader = ServiceLoader.load(LanguageService.class);
    for (LanguageService service : loader) {
      System.out.println(service.getLSName());
      System.out.println(service.getType());
      System.out.println(Arrays.toString(service.diagnose(new byte[]{'0'})));
    }
  }
}
```

```sh
BUILD_ROOT=build.spi
MOD=app
LIB_DIR=lib
cp "$BUILD_ROOT/proto/spi-lsp.jar" "$MOD/$LIB_DIR"
cp "$BUILD_ROOT/provider/spi-lsp-impl.jar" "$MOD/$LIB_DIR"
javac -cp "$MOD/$LIB_DIR/*" -d "$BUILD_ROOT/$MOD" -sourcepath "$MOD" "$MOD"/korhal/spi/lsp/client/*.java

# Run
java -cp "$BUILD_ROOT/$MOD:$MOD/$LIB_DIR/*" korhal.spi.lsp.client.MyNeovim

SPI Tests:
C_LS
C
[Ex1_1:10:5: error: use of undeclared identifier 'a', Ex1_1:25:9: warning: using the result of an assignment as a condition without parentheses [-Wparentheses]]
Java_LS
Java
[StopwatchExtension.java:[41,2] Type mismatch: cannot convert from long to boolean [16777233], BaseTest.java:[85,9] The value of the local variable i is not used [536870973]]
```


