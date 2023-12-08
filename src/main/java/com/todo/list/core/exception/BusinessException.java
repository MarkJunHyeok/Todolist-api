package com.todo.list.core.exception;

import static com.todo.list.core.exception.ExceptionCode.ERROR_SYSTEM;

import com.todo.list.core.response.ApiResult;
import com.todo.list.core.response.Result;
import java.util.Set;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class BusinessException extends RuntimeException {
  private static final long serialVersionUID = -6837907198565524472L;
  private ApiResult<?> apiResult = Result.ok().getBody();

  public BusinessException(final Throwable throwable) {
    this.setApiResult(throwable);
  }

  public BusinessException(final ExceptionCode exceptionCode) {
    super(exceptionCode.toString());
    this.setApiResult(exceptionCode);
  }

  private void setApiResult(final ExceptionCode exceptionCode) {
    this.apiResult = ApiResult.of(exceptionCode);
  }

  private void setApiResult(final Throwable throwable) {
    this.apiResult = ApiResult.of(ERROR_SYSTEM);
  }

  public boolean isEquals(final ExceptionCode exceptionCode) {
    return StringUtils.equals(this.apiResult.getCode(), exceptionCode.getCode());
  }

  public boolean isContains(final Set<ExceptionCode> sets) {
    return sets.stream().anyMatch(this::isEquals);
  }

  @Override
  public String toString() {
    return this.apiResult.toString();
  }
}
