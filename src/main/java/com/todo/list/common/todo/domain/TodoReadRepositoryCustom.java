package com.todo.list.common.todo.domain;

import com.todo.list.common.user.domain.User;
import java.time.LocalDate;
import java.util.List;

public interface TodoReadRepositoryCustom {

  List<Todo> getTodoList(User user, TodoStatus status, TodoType type, LocalDate date);
}
