package com.hfxb.app.core.annotation;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface TableBind {
    /**table name*/
    String name();
    /**primary key*/
    String pk() default "";
    /**foreign key*/
    String fk() default "";
}
