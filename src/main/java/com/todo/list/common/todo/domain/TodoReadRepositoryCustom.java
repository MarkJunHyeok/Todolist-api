package com.todo.list.common.todo.domain;

import java.time.LocalDate;
import java.util.List;

public interface TodoReadRepositoryCustom {

  List<Todo> getTodoList(TodoStatus status, TodoType type, LocalDate date);
}
