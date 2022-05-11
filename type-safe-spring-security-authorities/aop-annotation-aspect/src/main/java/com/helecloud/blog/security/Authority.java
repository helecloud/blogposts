package com.helecloud.blog.security;

public enum Authority {
  READ("my.service/read"),
  WRITE("my.service/write");

  private final String value;

  Authority(final String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "Authority{" +
        "name='" + name() + '\'' +
        ", value='" + value + '\'' +
        '}';
  }
}
