package korhal.spi.lsp;

public interface LanguageService {
  String getLSName();

  String getType();

  String[] diagnose(byte[] codes);
}
