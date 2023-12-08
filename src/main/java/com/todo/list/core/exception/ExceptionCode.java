package com.todo.list.core.exception;

import java.text.MessageFormat;
import lombok.Getter;

@Getter
public enum ExceptionCode {
  SUCCESS_NORMAL("S000", "성공"),

  ERROR_SYSTEM("E000", "이용에 불편을 드려 죄송합니다.\n요청하신 페이지에 오류가 발생하였습니다.\n잠시후에 다시 시도해 주세요.");

  private final String code;
  private final String message;

  ExceptionCode(final String code, final String message) {
    this.code = code;
    this.message = message;
  }

  @Override
  public String toString() {
    return MessageFormat.format("{0}({1}, {2})", this.name(), this.code, this.message);
  }
}
