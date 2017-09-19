package com.youximao.sdk.lib.database.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface _DB_METHOD {

    MethodEnum method() default MethodEnum._OPT_QUERY;

    enum MethodEnum {
        _OPT_INSERT, _OPT_DELETE, _OPT_UPDATE, _OPT_QUERY
    }
}
