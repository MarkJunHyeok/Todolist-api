package com.todo.list.controller;

import static com.todo.list.common.todo.domain.TodoStatus.IN_PROGRESS;
import static com.todo.list.common.todo.domain.TodoType.*;
import static com.todo.list.controller.dto.TodoCreateDTO.Request;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.todo.list.common.todo.domain.Todo;
import com.todo.list.common.todo.domain.TodoRepository;
import com.todo.list.controller.dto.TodoUpdateDTO;
import com.todo.list.core.support.BaseApiTest;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultMatcher;

class TodoControllerTest extends BaseApiTest {

  @Autowired private TodoRepository todoRepository;

  @Test
  void Todo_생성_성공() throws Exception {
    final Request request = new Request("description", SO_SO);

    this.mockMvc
        .perform(
            post("/todo")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpectAll(
            jsonPath("$.code").value("S000"),
            jsonPath("$.message").value("성공"),
            jsonPath("$.data.id").isNotEmpty(),
            jsonPath("$.data.description").value("description"),
            jsonPath("$.data.type").value(SO_SO.name()),
            jsonPath("$.data.status").value(IN_PROGRESS.name()),
            jsonPath("$.data.createdAt").isNotEmpty());
  }

  @Test
  void Todo_수정_성공() throws Exception {
    final Todo todo = this.todoRepository.save(new Todo("description", SO_SO));

    final TodoUpdateDTO.Request request = new TodoUpdateDTO.Request("description", SO_SO);

    this.mockMvc
        .perform(
            put("/todo/{id}", todo.getId())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpectAll(jsonPath("$.code").value("S000"), jsonPath("$.message").value("성공"));
  }

  @Test
  void Todo_완료() throws Exception {
    final Todo todo = this.todoRepository.save(new Todo("description", SO_SO));

    this.mockMvc.perform(patch("/todo/complete/{id}", todo.getId())).andExpect(status().isOk());
  }

  @Test
  void Todo_완료_취소() throws Exception {
    Todo todo = new Todo("description", SO_SO);

    todo.complete();

    todo = this.todoRepository.save(todo);

    this.mockMvc.perform(patch("/todo/un-complete/{id}", todo.getId())).andExpect(status().isOk());
  }

  @Test
  void Todo_삭제() throws Exception {
    final Todo todo = this.todoRepository.save(new Todo("description", SO_SO));

    this.mockMvc.perform(delete("/todo/{id}", todo.getId())).andExpect(status().isOk());

    final Optional<Todo> foundTodo = this.todoRepository.findById(todo.getId());

    assertThat(foundTodo.isEmpty()).isTrue();
  }

  @Test
  void Todolist_조회_성공() throws Exception {
    final Todo todo1 = new Todo("GOOD", SO_SO);
    final Todo todo2 = new Todo("GOOD", SO_SO);
    final Todo todo3 = new Todo("FAKE", LEISURELY);

    todoRepository.save(todo1);
    todoRepository.save(todo2);
    todoRepository.save(todo3);

    this.mockMvc
        .perform(
            get("/todo")
                .param("status", IN_PROGRESS.name())
                .param("type", IMPORTANT.name())
                .param("date", LocalDate.now().toString()))
        .andExpect(status().isOk())
        .andExpectAll(this.TodoList_조회_검증(todo1, 1))
        .andExpectAll(this.TodoList_조회_검증(todo2, 0));
  }

  @Test
  void Todo_단건_조회_성공() throws Exception {
    final Todo todo = this.todoRepository.save(new Todo("description", SO_SO));

    this.mockMvc
        .perform(get("/todo/{id}", todo.getId()))
        .andExpectAll(
            status().isOk(),
            jsonPath("$.code").value("S000"),
            jsonPath("$.message").value("성공"),
            jsonPath("$.data.id").isNotEmpty(),
            jsonPath("$.data.description").value("description"),
            jsonPath("$.data.type").value(SO_SO.name()),
            jsonPath("$.data.status").value(IN_PROGRESS.name()),
            jsonPath("$.data.createdAt").isNotEmpty());
  }

  private ResultMatcher[] TodoList_조회_검증(final Todo todo, final int index) {
    return List.of(
            jsonPath("$.data.[" + index + "].id").value(todo.getId()),
            jsonPath("$.data.[" + index + "].description").value(todo.getDescription()),
            jsonPath("$.data.[" + index + "].type").value(todo.getType().name()),
            jsonPath("$.data.[" + index + "].status").value(todo.getStatus().name()),
            jsonPath("$.data.[" + index + "].createdAt").isNotEmpty())
        .toArray(ResultMatcher[]::new);
  }
}
