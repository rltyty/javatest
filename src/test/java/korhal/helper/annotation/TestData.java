package korhal.helper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import korhal.helper.loader.TestDataLoader;
import korhal.helper.provider.TestDataProvider;

@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@ParameterizedTest(name = "[{index}]")
@ArgumentsSource(TestDataProvider.class)
public @interface TestData {
  Class<?> type(); // record/class to load
  String path();   // file path
  @SuppressWarnings("rawtypes")
  Class<? extends TestDataLoader> loader(); // annotation elements cannot be parameterized
}
