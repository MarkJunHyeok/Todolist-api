package com.todo.list.common.todo.domain;

import static com.todo.list.common.todo.domain.TodoStatus.IN_PROGRESS;
import static com.todo.list.common.todo.domain.TodoType.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.todo.list.core.support.BaseServiceTest;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TodoReadRepositoryImplTest extends BaseServiceTest {

  @Autowired private TodoReadRepositoryCustom todoReadRepositoryCustom;

  @Autowired private TodoRepository todoRepository;

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
        this.todoReadRepositoryCustom.getTodoList(IN_PROGRESS, IMPORTANT, LocalDate.now());
    final List<Todo> yesterdayTodoList =
        this.todoReadRepositoryCustom.getTodoList(
            IN_PROGRESS, IMPORTANT, LocalDate.now().minusDays(1));

    assertThat(todoList).containsExactly(todo2, todo1);
    assertThat(todoList).doesNotContain(yesterdayTodo1, yesterdayTodo2, yesterdayTodo3, todo3);
    assertThat(yesterdayTodoList).containsExactly(yesterdayTodo2, yesterdayTodo1);
    assertThat(yesterdayTodoList).doesNotContain(yesterdayTodo3, todo1, todo2, todo3);
  }
}
