package korhal.lang.annotation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

@Retention(RetentionPolicy.RUNTIME)
@interface MyAnnotation {
    String name();
}

public class AnnotationTest {

  @MyAnnotation(name = "C1")
  class TestClass1 {
  }

  @Test
  public void class_annotation_test () {
    MyAnnotation annotation = TestClass1.class.getAnnotation(MyAnnotation.class);

    assertNotNull(annotation, "Annotation not present on TestClass1");
    assertEquals("C1", annotation.name(), "Annotation name mismatch");
  }

  @Test
  @MyAnnotation(name = "M1")
  public void method_annotation_test () throws NoSuchMethodException {
    Method m = AnnotationTest.class.getMethod("method_annotation_test");
    MyAnnotation annotation = m.getAnnotation(MyAnnotation.class);
    assertNotNull(annotation, "Annotation not present on method_annotation_test()");
    assertEquals("M1", annotation.name(), "Annotation name mismatch");
  }
}

