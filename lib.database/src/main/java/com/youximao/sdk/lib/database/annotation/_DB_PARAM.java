package com.youximao.sdk.lib.database.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface _DB_PARAM {

    String _PARAM_SQL = "paramSql";
    String _PARAM_VALUE = "paramValue";

    String value();
}
