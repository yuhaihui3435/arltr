package com.neusoft.arltr.common.entity.user;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

@Retention(RUNTIME)
public @interface EnumerationValueName {
 String type() default "";
 String valueFieldName() default "";
}
