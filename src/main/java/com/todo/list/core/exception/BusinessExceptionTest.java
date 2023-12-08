package com.todo.list.core.exception;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.assertj.core.api.ThrowableAssert;
import org.assertj.core.api.ThrowableTypeAssert;

public final class BusinessExceptionTest {

  private final ExceptionCode exceptionCode;
  private final ThrowableTypeAssert<BusinessException> throwableTypeAssert;

  public BusinessExceptionTest(
      final ExceptionCode exceptionCode,
      final ThrowableTypeAssert<BusinessException> throwableTypeAssert) {
    this.exceptionCode = exceptionCode;
    this.throwableTypeAssert = throwableTypeAssert;
  }

  public static BusinessExceptionTest assertThatBusinessException(final ExceptionCode errorCode) {
    return new BusinessExceptionTest(errorCode, assertThatExceptionOfType(BusinessException.class));
  }

  public void isThrownBy(final ThrowableAssert.ThrowingCallable throwingCallable) {
    this.throwableTypeAssert
        .isThrownBy(throwingCallable)
        .withMessage(this.exceptionCode.getMessage());
  }
}
