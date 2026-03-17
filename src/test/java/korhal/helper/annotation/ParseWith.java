package korhal.helper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import korhal.helper.parser.DataType;

@Target(ElementType.RECORD_COMPONENT) 
@Retention(RetentionPolicy.RUNTIME)
public @interface ParseWith {
  DataType value();
}
