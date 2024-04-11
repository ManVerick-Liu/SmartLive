package com.MacrohardStudio.annotation;

import java.lang.annotation.*;

/**
 *
 * 接口不需要通过Token验证
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WithoutJWT{
    //申明了一个属性配置项
    boolean required() default true;
}