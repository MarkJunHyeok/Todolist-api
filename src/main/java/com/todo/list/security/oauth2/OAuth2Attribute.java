package com.todo.list.security.oauth2;

import static lombok.AccessLevel.PRIVATE;

import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder(access = PRIVATE)
public class OAuth2Attribute {
  private Map<String, Object> attributes;
  private String attributeKey;
  private String socialId;
  private String name;

  static OAuth2Attribute of(String provider, String attributeKey, Map<String, Object> attributes) {
    switch (provider) {
      case "github":
        return ofGithub(attributeKey, attributes);
      default:
        throw new RuntimeException();
    }
  }

  private static OAuth2Attribute ofGithub(String attributeKey, Map<String, Object> attributes) {
    return OAuth2Attribute.builder()
        .name((String) attributes.get("name"))
        .socialId((String.valueOf(attributes.get("id"))))
        .attributes(attributes)
        .attributeKey(attributeKey)
        .build();
  }

  Map<String, Object> convertToMap() {
    Map<String, Object> map = new HashMap<>();
    map.put("id", attributeKey);
    map.put("key", attributeKey);
    map.put("name", name);
    map.put("socialId", socialId);

    return map;
  }
}
