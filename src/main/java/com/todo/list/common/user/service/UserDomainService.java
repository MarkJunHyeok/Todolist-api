package com.todo.list.common.user.service;

import com.todo.list.common.user.domain.User;
import com.todo.list.common.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserDomainService implements UserService {

  private final UserRepository userRepository;

  public User loadUser(Long socialId) {
    Optional<User> userOptional = userRepository.findBySocialId(socialId);

    if (userOptional.isPresent()) {
      return userOptional.get();
    } else {
      User newUser = new User(socialId);

      return userRepository.save(newUser);
    }
  }
}
