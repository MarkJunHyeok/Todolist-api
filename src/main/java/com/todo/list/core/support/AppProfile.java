package com.todo.list.core.support;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import org.springframework.context.annotation.Profile;

@Profile({"sandbox", "qa", "prod"})
@Retention(RUNTIME)
public @interface AppProfile {}
