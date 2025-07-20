package korhal.spi.lsp;

public interface LanguageService {
  public String getLSName();

  public String getType();

  public String[] diagnose(byte[] codes);
}
