package korhal.helper.provider;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.ParameterDeclarations;

import korhal.helper.annotation.TestData;
import korhal.helper.loader.TestDataLoader;

public class TestDataProvider implements ArgumentsProvider {

  @Override
  public Stream<? extends Arguments> provideArguments(
      ParameterDeclarations parameters, ExtensionContext ctx) {
    TestData data = ctx.getTestMethod()
        .flatMap(m -> Optional.ofNullable(m.getAnnotation(TestData.class)))
        .orElseThrow(() -> new IllegalArgumentException("Missing @TestData annotation"));
    Class<?> testClass = ctx.getRequiredTestClass();
    try {
      return loadData(data, testClass).stream().map(Arguments::of);
    } catch (Exception e) {
      throw new RuntimeException("Failed to load test data for: " + data.path(), e);
    }
  }

  @SuppressWarnings("unchecked")
  private <T> List<T> loadData(TestData data, Class<?> testClass) throws Exception {
    TestDataLoader<T> loader = (TestDataLoader<T>) data.loader()
        .getDeclaredConstructor(Class.class)
        .newInstance(data.type());
    return loader.load(data.path(), testClass);
  }

}
