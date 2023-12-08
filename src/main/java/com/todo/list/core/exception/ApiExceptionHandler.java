package com.todo.list.core.exception;

import com.todo.list.core.response.ApiResult;
import com.todo.list.core.response.Result;
import com.todo.list.core.util.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

  // 아래서 놓친 예외가 있을때 이곳으로 확인하기 위해 존재한다.
  // 놓친 예외는 이곳에서 확인하여 추가해주면 된다.
  @ExceptionHandler({Throwable.class})
  public ResponseEntity<ApiResult<?>> exception(final Throwable e) {
    log.warn(LogUtils.getStackTrace(e));
    return Result.error();
  }

  @ExceptionHandler({BusinessException.class})
  public ResponseEntity<ApiResult<?>> businessException(final BusinessException e) {
    final String stacksOfProject = LogUtils.getStackTrace(e);
    if (StringUtils.startsWith(e.getApiResult().getCode(), "E")) {
      log.error(stacksOfProject);
    } else {
      log.info(stacksOfProject);
    }
    return Result.error(e);
  }
}
