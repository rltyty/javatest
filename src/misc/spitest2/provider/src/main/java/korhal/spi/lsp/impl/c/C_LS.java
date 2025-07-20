package korhal.spi.lsp.impl.c;

import korhal.spi.lsp.LanguageService;

public class C_LS implements LanguageService {
  @Override
  public String getLSName() {
    return "C Language Server";
  }

  @Override
  public String getType() {
    return "C";
  }

  @Override
  public String[] diagnose(byte[] codes) {
    return new String[] {
      "Ex1_1:10:5: error: use of undeclared identifier 'a'",
      "Ex1_1:25:9: warning: using the result of an assignment as a condition without parentheses [-Wparentheses]",
    };
  }
}
