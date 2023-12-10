package com.todo.list.security.user;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TodoUserException extends RuntimeException {

  private String message;
  private HttpStatus status;

  public TodoUserException(String message, HttpStatus status) {
    super(message);
    this.message = message;
    this.status = status;
  }
}
