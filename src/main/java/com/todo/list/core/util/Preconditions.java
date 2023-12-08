package com.todo.list.core.util;

import com.todo.list.core.exception.BusinessException;
import com.todo.list.core.exception.ExceptionCode;
import java.text.MessageFormat;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

public class Preconditions {

  public static <T> T notNull(T obj) {
    return Objects.requireNonNull(obj);
  }

  public static void require(Object value) {
    if (value instanceof String str) {
      requireString(str);
    }
    if (value == null) {
      throw new IllegalArgumentException();
    }
  }

  public static void require(Object value, String message) {
    if (value instanceof String str) {
      requireString(str, message);
    }
    if (value == null) {
      throw new IllegalArgumentException(message);
    }
  }

  private static void requireString(String value) {
    if (StringUtils.isBlank(value)) {
      throw new IllegalArgumentException();
    }
  }

  private static void requireString(String value, String message) {
    if (StringUtils.isBlank(value)) {
      throw new IllegalArgumentException(message);
    }
  }

  public static void check(boolean expression) {
    if (!expression) {
      throw new IllegalStateException();
    }
  }

  public static void check(boolean expression, String message) {
    if (!expression) {
      throw new IllegalStateException(message);
    }
  }

  public static void check(boolean expression, String message, Object... args) {
    if (!expression) {
      throw new IllegalStateException(MessageFormat.format(message, args));
    }
  }

  public static void validate(boolean expression, ExceptionCode exceptionCode) {
    if (!expression) {
      throw new BusinessException(exceptionCode);
    }
  }
}
