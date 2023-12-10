package com.todo.list.common.user.domain;

import static lombok.AccessLevel.PROTECTED;

import com.todo.list.core.support.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class User extends BaseEntity {

  private Long socialId;

  public User(Long socialId) {
    this.socialId = socialId;
  }
}
