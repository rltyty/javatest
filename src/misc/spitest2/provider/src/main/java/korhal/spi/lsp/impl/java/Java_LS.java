package korhal.spi.lsp.impl.java;

import korhal.spi.lsp.LanguageService;

public class Java_LS implements LanguageService {
  @Override
  public String getLSName() {
    return "Java Language Server";
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
