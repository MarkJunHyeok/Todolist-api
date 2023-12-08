package com.todo.list.controller;

import com.todo.list.common.todo.domain.Todo;
import com.todo.list.common.todo.service.TodoService;
import com.todo.list.controller.dto.GetTodoDTO;
import com.todo.list.controller.dto.GetTodoListDTO;
import com.todo.list.controller.dto.TodoCreateDTO;
import com.todo.list.controller.dto.TodoCreateDTO.Request;
import com.todo.list.controller.dto.TodoCreateDTO.Response;
import com.todo.list.controller.dto.TodoUpdateDTO;
import com.todo.list.core.response.ApiResult;
import com.todo.list.core.response.Result;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todo")
public class TodoController {

  private final TodoService todoService;

  @PostMapping
  public ResponseEntity<ApiResult<TodoCreateDTO.Response>> create(@RequestBody final Request req) {
    final Todo todo = this.todoService.create(req.description(), req.type());

    final Response response = new Response(todo);

    return Result.created(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResult<?>> update(
      @PathVariable final Long id, @RequestBody final TodoUpdateDTO.Request req) {
    this.todoService.update(id, req.description(), req.type());

    return Result.ok();
  }

  @PatchMapping("/complete/{id}")
  public ResponseEntity<ApiResult<?>> complete(@PathVariable final Long id) {
    this.todoService.complete(id);

    return Result.ok();
  }

  @PatchMapping("/un-complete/{id}")
  public ResponseEntity<ApiResult<?>> unComplete(@PathVariable final Long id) {
    this.todoService.unComplete(id);

    return Result.ok();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResult<?>> delete(@PathVariable final Long id) {
    this.todoService.delete(id);

    return Result.ok();
  }

  @GetMapping
  public ResponseEntity<ApiResult<List<GetTodoListDTO.Response>>> getTodoList(
      final GetTodoListDTO.Request request) {
    final List<GetTodoListDTO.Response> todoList =
        this.todoService.getTodoList(request.status(), request.type(), request.date()).stream()
            .map(GetTodoListDTO.Response::new)
            .toList();

    return Result.ok(todoList);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResult<GetTodoDTO.Response>> getTodo(@PathVariable final Long id) {
    Todo todo = this.todoService.getTodo(id);

    return Result.ok(new GetTodoDTO.Response(todo));
  }
}
