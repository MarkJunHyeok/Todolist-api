package com.todo.list.common.todo.service;

import static com.todo.list.core.util.Preconditions.validate;

import com.todo.list.common.todo.domain.*;
import com.todo.list.common.user.domain.User;
import com.todo.list.common.user.domain.UserRepository;
import com.todo.list.core.exception.ExceptionCode;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class DomainTodoService implements TodoService {

  private final TodoRepository todoRepository;
  private final TodoReadRepositoryCustom todoReadRepositoryCustom;
  private final UserRepository userRepository;

  @Override
  public Todo create(final Long userId, final String description, final TodoType type) {
    final User user = userRepository.findById(userId).orElseThrow();
    final Todo todo = new Todo(user, description, type);

    return this.todoRepository.save(todo);
  }

  @Override
  public void update(Long todoId, String description, TodoType type) {
    final Todo todo = this.todoRepository.findById(todoId).orElseThrow();

    todo.update(description, type);
  }

  @Override
  public void complete(final Long todoId) {
    final Todo todo = this.todoRepository.findById(todoId).orElseThrow();

    todo.complete();
  }

  @Override
  public void unComplete(final Long todoId) {
    final Todo todo = this.todoRepository.findById(todoId).orElseThrow();

    todo.unComplete();
  }

  @Override
  public void delete(final Long todoId) {
    final Todo todo = this.todoRepository.findById(todoId).orElseThrow();

    validate(todo.isDeletable(), ExceptionCode.ERROR_SYSTEM);

    this.todoRepository.delete(todo);
  }

  @Override
  public List<Todo> getTodoList(
      final Long userId, final TodoStatus status, final TodoType type, final LocalDate date) {
    User user = userRepository.findById(userId).orElseThrow();
    return this.todoReadRepositoryCustom.getTodoList(user, status, type, date);
  }

  @Override
  public Todo getTodo(Long id) {
    return todoRepository.findById(id).orElseThrow();
  }
}
