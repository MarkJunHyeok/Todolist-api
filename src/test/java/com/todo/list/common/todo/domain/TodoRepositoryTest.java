package com.todo.list.common.todo.domain;

import static com.todo.list.common.todo.domain.TodoStatus.IN_PROGRESS;
import static com.todo.list.common.todo.domain.TodoType.SO_SO;
import static org.assertj.core.api.Assertions.assertThat;

import com.todo.list.common.user.domain.User;
import com.todo.list.common.user.domain.UserRepository;
import com.todo.list.core.support.BaseRepositoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TodoRepositoryTest extends BaseRepositoryTest {

  @Autowired private TodoRepository todoRepository;
  @Autowired private UserRepository userRepository;

  private User user;

  @BeforeEach
  void setUp() {
    this.user = userRepository.save(new User(1L));
  }

  @Test
  void Todo_생성_성공() {
    Todo todo = new Todo(user, "description", SO_SO);

    todo = this.todoRepository.save(todo);

    final Todo foundTodo = this.todoRepository.findById(todo.getId()).orElseThrow();

    assertThat(foundTodo.getId()).isNotNull();
    assertThat(foundTodo.getDescription()).isEqualTo("description");
    assertThat(foundTodo.getType()).isEqualTo(SO_SO);
    assertThat(foundTodo.getStatus()).isEqualTo(IN_PROGRESS);
    assertThat(foundTodo.getCreatedAt()).isNotNull();
  }
}
