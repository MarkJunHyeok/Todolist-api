package com.todo.list.common.user.service;

import com.todo.list.common.user.domain.User;

public interface UserService {

  User loadUser(Long socialId);
}
