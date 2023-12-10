package com.todo.list.common.todo.service;

import com.todo.list.common.todo.domain.Todo;
import com.todo.list.common.todo.domain.TodoStatus;
import com.todo.list.common.todo.domain.TodoType;
import java.time.LocalDate;
import java.util.List;

public interface TodoService {
  Todo create(Long userId, String description, TodoType type);

  void update(Long todoId, String description, TodoType type);

  void complete(Long todoId);

  void unComplete(Long todoId);

  void delete(Long todoId);

  List<Todo> getTodoList(Long userId, TodoStatus status, TodoType type, LocalDate date);

  Todo getTodo(Long id);
}
