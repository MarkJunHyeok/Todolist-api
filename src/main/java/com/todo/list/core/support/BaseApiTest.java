package com.todo.list.core.support;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.list.common.user.domain.User;
import com.todo.list.common.user.domain.UserRepository;
import com.todo.list.security.user.DefaultTodoUser;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

@Transactional
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class BaseApiTest {

  @Autowired protected MockMvc mockMvc;

  @Autowired protected ObjectMapper objectMapper;
  @Autowired protected UserRepository userRepository;

  protected void login(User user) {
    login(user.getId());
  }

  private void login(Long id) {
    DefaultTodoUser user =
        (DefaultTodoUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    user.setId(id);
  }
}
