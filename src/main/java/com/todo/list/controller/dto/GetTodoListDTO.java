package com.todo.list.controller.dto;

import com.todo.list.common.todo.domain.Todo;
import com.todo.list.common.todo.domain.TodoStatus;
import com.todo.list.common.todo.domain.TodoType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class GetTodoListDTO {

  public record Request(
      /** 타입 */
      @NotNull TodoType type,
      /** 상태 */
      @NotNull TodoStatus status,
      /** 일자 */
      @NotNull LocalDate date) {}

  @Getter
  @AllArgsConstructor
  public static class Response {
    /** 아이디 */
    @NotNull private final Long id;
    /** 내용 */
    @NotBlank private final String description;
    /** 타입 */
    @NotNull private final TodoType type;
    /** 상태 */
    @NotNull private final TodoStatus status;
    /** 생성일 */
    @NotNull private final Instant createdAt;

    public Response(final Todo todo) {
      this.id = todo.getId();
      this.description = todo.getDescription();
      this.type = todo.getType();
      this.status = todo.getStatus();
      this.createdAt = todo.getCreatedAt();
    }
  }
}
