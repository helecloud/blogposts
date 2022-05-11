package com.helecloud.blog.rest;

import com.helecloud.blog.security.Authority;
import com.helecloud.blog.security.HasAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

  @HasAuthority(hasAny = {Authority.READ})
  @GetMapping("/")
  public void get() {
    System.out.println("OK");
  }

  @HasAuthority(hasAny = {Authority.WRITE})
  @PostMapping("/")
  public void post() {
    System.out.println("OK");
  }

}
