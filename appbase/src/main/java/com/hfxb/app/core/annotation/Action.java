package com.hfxb.app.core.annotation;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Action {
	/**controller action*/
    String action();
    
    String view() default "";
}
