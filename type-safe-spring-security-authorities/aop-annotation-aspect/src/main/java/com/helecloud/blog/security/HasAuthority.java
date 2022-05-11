package com.helecloud.blog.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface HasAuthority {

  /**
   * The annotated method invocation will proceed if any of the hasAny items is available as a
   * GrantedAuthority in the Authentication object; default - {}
   */
  Authority[] hasAny() default {};

  /**
   * The annotated method invocation will proceed if and only if all the hasAll items are available
   * as a GrantedAuthorities in the Authentication object. An empty set of authorities will not
   * grant any authorization; default - {}
   */
  Authority[] hasAll() default {};

}
