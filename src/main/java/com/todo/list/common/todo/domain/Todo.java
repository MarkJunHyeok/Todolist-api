package com.todo.list.common.todo.domain;

import static com.todo.list.common.todo.domain.TodoStatus.IN_PROGRESS;
import static com.todo.list.core.util.Preconditions.*;
import static lombok.AccessLevel.PROTECTED;

import com.todo.list.common.user.domain.User;
import com.todo.list.core.support.BaseEntity;
import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Todo extends BaseEntity {

  @ManyToOne private User user;

  /* 내용 */
  private String description;

  /* 상태 */
  @Enumerated(EnumType.STRING)
  private TodoType type;

  /* 상태 */
  @Enumerated(EnumType.STRING)
  private TodoStatus status = IN_PROGRESS;

  public Todo(final User user, final String description, final TodoType type) {
    require(user);
    require(description);
    require(type);

    this.user = user;
    this.description = description;
    this.type = type;
  }

  public void update(final String description, final TodoType type) {
    require(description);
    require(type);

    this.description = description;
    this.type = type;
  }

  public void complete() {
    check(this.status == IN_PROGRESS);

    this.status = TodoStatus.COMPLETED;
  }

  public void unComplete() {
    check(this.status == TodoStatus.COMPLETED);

    this.status = IN_PROGRESS;
  }

  public boolean isDeletable() {
    final Instant createdAt = this.getCreatedAt();
    final LocalDate currentDate = LocalDate.now();
    final LocalDate targetDate = createdAt.atZone(ZoneId.systemDefault()).toLocalDate();

    return !targetDate.isBefore(currentDate);
  }
}
