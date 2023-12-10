package com.todo.list.common.todo.domain;

import static com.todo.list.common.todo.domain.TodoStatus.IN_PROGRESS;
import static com.todo.list.common.todo.domain.TodoType.SO_SO;
import static org.assertj.core.api.Assertions.*;

import com.todo.list.common.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class TodoTest {

  private Todo todo;
  private User user;

  @BeforeEach
  void setUp() {
    this.user = new User(1L);
    this.todo = new Todo(user, "description", SO_SO);
  }

  @Test
  void Todo_생성_성공() {
    final Todo todo = new Todo(user, "description", SO_SO);

    assertThat(todo.getUser()).isEqualTo(user);
    assertThat(todo.getDescription()).isEqualTo("description");
    assertThat(todo.getType()).isEqualTo(SO_SO);
    assertThat(todo.getStatus()).isEqualTo(IN_PROGRESS);
    assertThat(todo.getCreatedAt()).isNotNull();
  }

  @Test
  void Todo_생성_실패__유저가_null() {
    assertThatIllegalArgumentException()
        .isThrownBy(
            () -> {
              new Todo(null, "description", SO_SO);
            });
  }

  @ParameterizedTest
  @NullAndEmptySource
  void Todo_생성_실패__description이_널_혹은_빈값(final String description) {
    assertThatIllegalArgumentException()
        .isThrownBy(
            () -> {
              new Todo(user, description, SO_SO);
            });
  }

  @Test
  void Todo_생성_실패__description이_공백() {
    assertThatIllegalArgumentException()
        .isThrownBy(
            () -> {
              new Todo(user, " ", SO_SO);
            });
  }

  @Test
  void Todo_생성_실패__타입이_null() {
    assertThatIllegalArgumentException()
        .isThrownBy(
            () -> {
              new Todo(user, "description", null);
            });
  }

  @Test
  void Todo_수정() {
    todo.update("description", SO_SO);

    assertThat(todo.getDescription()).isEqualTo("description");
    assertThat(todo.getType()).isEqualTo(SO_SO);
  }

  @ParameterizedTest
  @NullAndEmptySource
  void Todo_수정_실패__description이_널_혹은_빈값(final String description) {
    assertThatIllegalArgumentException()
        .isThrownBy(
            () -> {
              todo.update(description, SO_SO);
            });
  }

  @Test
  void Todo_수정_실패__description이_공백() {
    assertThatIllegalArgumentException()
        .isThrownBy(
            () -> {
              todo.update(" ", SO_SO);
            });
  }

  @Test
  void Todo_수정_실패__타입이_null() {
    assertThatIllegalArgumentException()
        .isThrownBy(
            () -> {
              todo.update("description", null);
            });
  }

  @Test
  void Todo_완료() {
    this.todo.complete();

    assertThat(this.todo.getStatus()).isEqualTo(TodoStatus.COMPLETED);
  }

  @Test
  void Todo_완료_실패__진행중_상태가_아님() {
    this.todo.complete();

    assertThatIllegalStateException().isThrownBy(() -> this.todo.complete());
  }

  @Test
  void Todo_완료_취소() {
    this.todo.complete();

    this.todo.unComplete();

    assertThat(this.todo.getStatus()).isEqualTo(IN_PROGRESS);
  }

  @Test
  void Todo_완료_취소_실패__완료됨_상태가_아님() {
    assertThatIllegalStateException().isThrownBy(() -> this.todo.unComplete());
  }
}
