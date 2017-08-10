package com.ra1ph.giphysample.utils;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Mikhail Korshunov on 10.08.17.
 */

@Scope
@Retention(RUNTIME)
public @interface PerActivity {}
