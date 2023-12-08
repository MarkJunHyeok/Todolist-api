package com.todo.list.core.util;

import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.exception.ExceptionUtils;

@UtilityClass
public class LogUtils {
  public String getStackTrace(final Throwable e) {
    return Arrays.stream(ExceptionUtils.getStackFrames(e))
        .filter(item -> item.startsWith("\tat com.todo.list") || !item.startsWith("\tat"))
        .collect(Collectors.joining("\n"));
  }
}
