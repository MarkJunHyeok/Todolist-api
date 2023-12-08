package com.todo.list.common.todo.domain;

import static com.todo.list.common.todo.domain.TodoType.*;
import static com.todo.list.common.todo.domain.TodoType.SO_SO;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoReadRepositoryImpl implements TodoReadRepositoryCustom {

  private final JPAQueryFactory queryFactory;
  private static final QTodo todo = QTodo.todo;

  @Override
  public List<Todo> getTodoList(
      final TodoStatus status, final TodoType type, final LocalDate date) {
    final LocalDateTime startDateTime = date.atStartOfDay();
    final LocalDateTime endDateTime = startDateTime.plusDays(1);

    final ZonedDateTime startZonedDateTime = startDateTime.atZone(ZoneId.systemDefault());
    final ZonedDateTime endZonedDateTime = endDateTime.atZone(ZoneId.systemDefault());

    final BooleanBuilder booleanBuilder = new BooleanBuilder();

    booleanBuilder.and(
        todo.createdAt.between(startZonedDateTime.toInstant(), endZonedDateTime.toInstant()));
    booleanBuilder.and(eqStatus(status));
    booleanBuilder.and(eqType(type));

    final JPAQuery<Todo> query =
        this.queryFactory.selectFrom(todo).where(booleanBuilder).orderBy(todo.id.desc());

    return query.fetch();
  }

  private BooleanExpression eqStatus(final TodoStatus status) {
    if (Objects.isNull(status)) {
      return null;
    }

    return todo.status.eq(status);
  }

  private BooleanExpression eqType(final TodoType type) {
    if (Objects.isNull(type)) {
      return null;
    }
    if (type == IMPORTANT) {
      return todo.type.in(VERY_IMPORTANT, IMPORTANT, SO_SO);
    }
    return todo.type.in(VERY_LEISURELY, LEISURELY, SO_SO);
  }
}
