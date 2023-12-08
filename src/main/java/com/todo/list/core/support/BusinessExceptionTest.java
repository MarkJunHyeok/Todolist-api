package com.todo.list.core.support;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.todo.list.core.exception.BusinessException;
import com.todo.list.core.exception.ExceptionCode;
import org.assertj.core.api.ThrowableAssert;
import org.assertj.core.api.ThrowableTypeAssert;

public final class BusinessExceptionTest {
  private final ExceptionCode exceptionCode;
  private final ThrowableTypeAssert<BusinessException> throwableTypeAssert;

  public BusinessExceptionTest(
      ExceptionCode exceptionCode, ThrowableTypeAssert<BusinessException> throwableTypeAssert) {
    this.exceptionCode = exceptionCode;
    this.throwableTypeAssert = throwableTypeAssert;
  }

  public static BusinessExceptionTest assertThatBusinessException(ExceptionCode exceptionCode) {
    return new BusinessExceptionTest(
        exceptionCode, assertThatExceptionOfType(BusinessException.class));
  }

  public void isThrownBy(ThrowableAssert.ThrowingCallable throwingCallable) {
    throwableTypeAssert.isThrownBy(throwingCallable);
  }
}
