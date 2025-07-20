package korhal.spi.lsp.client;

import java.util.Arrays;
import java.util.ServiceLoader;
import korhal.spi.lsp.LanguageService;

public class MyNeovim {
  public static void main(String[] args) {
    System.out.println("SPI Tests:");
    ServiceLoader<LanguageService> loader = ServiceLoader.load(LanguageService.class);
    for (LanguageService service : loader) {
      System.out.println(service.getClass());
      System.out.println(service.getLSName());
      System.out.println(service.getType());
      System.out.println(Arrays.toString(service.diagnose(new byte[] {'0'})));
    }
  }
}
