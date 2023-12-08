package com.todo.list.common.todo.service;

import static com.todo.list.common.todo.domain.TodoStatus.IN_PROGRESS;
import static com.todo.list.common.todo.domain.TodoType.*;
import static com.todo.list.common.todo.domain.TodoType.IMPORTANT;
import static com.todo.list.core.support.BusinessExceptionTest.assertThatBusinessException;
import static org.assertj.core.api.Assertions.assertThat;

import com.todo.list.common.todo.domain.Todo;
import com.todo.list.common.todo.domain.TodoRepository;
import com.todo.list.common.todo.domain.TodoStatus;
import com.todo.list.core.exception.ExceptionCode;
import com.todo.list.core.support.BaseServiceTest;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class DomainTodoServiceTest extends BaseServiceTest {

  @Autowired private TodoService todoService;

  @Autowired private TodoRepository todoRepository;

  private Todo todo;

  @BeforeEach
  void setUp() {
    this.todo = this.todoRepository.save(new Todo("description", SO_SO));
  }

  @Test
  void Todo_생성_성공() {
    final Todo todo = this.todoService.create("description", SO_SO);

    assertThat(todo.getId()).isNotNull();
    assertThat(todo.getDescription()).isEqualTo("description");
    assertThat(todo.getType()).isEqualTo(SO_SO);
    assertThat(todo.getStatus()).isEqualTo(IN_PROGRESS);
    assertThat(todo.getCreatedAt()).isNotNull();
  }

  @Test
  void Todo_수정() {
    this.todoService.update(this.todo.getId(), "description", SO_SO);

    assertThat(todo.getDescription()).isEqualTo("description");
    assertThat(todo.getType()).isEqualTo(SO_SO);
  }

  @Test
  void Todo_완료() {
    this.todoService.complete(this.todo.getId());

    assertThat(this.todo.getStatus()).isEqualTo(TodoStatus.COMPLETED);
  }

  @Test
  void Todo_완료_취소() {
    this.todoService.complete(this.todo.getId());

    this.todoService.unComplete(this.todo.getId());

    assertThat(this.todo.getStatus()).isEqualTo(IN_PROGRESS);
  }

  @Test
  void Todo_삭제() {
    this.todoService.delete(this.todo.getId());

    final Optional<Todo> foundTodo = this.todoRepository.findById(this.todo.getId());

    assertThat(foundTodo.isEmpty()).isTrue();
  }

  @Test
  void Todo_삭제_실패__삭졔_가능한_날이_지남() {
    final Instant yesterday = Instant.now().minus(1, ChronoUnit.DAYS);

    this.todo.setCreatedAt(yesterday);

    assertThatBusinessException(ExceptionCode.ERROR_SYSTEM)
        .isThrownBy(() -> this.todoService.delete(this.todo.getId()));
  }

  @Test
  void TodoList_조회_성공() {
    final Instant yesterday = Instant.now().minus(1, ChronoUnit.DAYS);

    final Todo yesterdayTodo1 = new Todo("GOOD", SO_SO);
    final Todo yesterdayTodo2 = new Todo("GOOD", SO_SO);
    final Todo yesterdayTodo3 = new Todo("FAKE", LEISURELY);
    final Todo todo1 = new Todo("GOOD", SO_SO);
    final Todo todo2 = new Todo("GOOD", SO_SO);
    final Todo todo3 = new Todo("FAKE", LEISURELY);

    yesterdayTodo1.setCreatedAt(yesterday);
    yesterdayTodo2.setCreatedAt(yesterday);
    yesterdayTodo3.setCreatedAt(yesterday);

    todoRepository.save(yesterdayTodo1);
    todoRepository.save(yesterdayTodo2);
    todoRepository.save(yesterdayTodo3);
    todoRepository.save(todo1);
    todoRepository.save(todo2);
    todoRepository.save(todo3);

    final List<Todo> todoList =
        this.todoService.getTodoList(IN_PROGRESS, IMPORTANT, LocalDate.now());
    final List<Todo> yesterdayTodoList =
        this.todoService.getTodoList(IN_PROGRESS, IMPORTANT, LocalDate.now().minusDays(1));

    assertThat(todoList).containsExactly(todo2, todo1, todo);
    assertThat(todoList).doesNotContain(yesterdayTodo1, yesterdayTodo2, yesterdayTodo3, todo3);
    assertThat(yesterdayTodoList).containsExactly(yesterdayTodo2, yesterdayTodo1);
    assertThat(yesterdayTodoList).doesNotContain(yesterdayTodo3, todo1, todo2, todo3);
  }
}
