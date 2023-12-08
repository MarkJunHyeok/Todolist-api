package com.todo.list.controller.dto;

import com.todo.list.common.todo.domain.TodoType;

public class TodoUpdateDTO {
  public record Request(
      /** 내용 */
      String description,
      /** 타입 */
      TodoType type) {}
}
