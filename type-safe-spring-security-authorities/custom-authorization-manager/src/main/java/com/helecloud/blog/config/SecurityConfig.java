package com.helecloud.blog.config;

import com.helecloud.blog.security.HasAuthority;
import com.helecloud.blog.security.HasAuthorityAuthorizationManager;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.Pointcuts;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.security.authorization.method.AuthorizationInterceptorsOrder;
import org.springframework.security.authorization.method.AuthorizationManagerBeforeMethodInterceptor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private static final String HAS_AUTHORITY_ANNOTATION_POINTCUT = "hasAuthorityAnnotationPointcut";

  @Bean
  public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
    http.authorizeRequests(authz -> authz.anyRequest().authenticated())
        .oauth2ResourceServer(
            oauth2 -> oauth2.jwt().jwtAuthenticationConverter(jwtAuthenticationConverter()));

    return http.build();
  }

  private JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    // clean the "SCOPE_" prefix from the granted authorities
    jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

    final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
    return jwtAuthenticationConverter;
  }

  @Bean
  @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
  public Advisor preAuthorize(
      final HasAuthorityAuthorizationManager authorizationManager,
      @Qualifier(HAS_AUTHORITY_ANNOTATION_POINTCUT) final Pointcut pointcut) {

    final AuthorizationManagerBeforeMethodInterceptor customInterceptor =
        new AuthorizationManagerBeforeMethodInterceptor(pointcut, authorizationManager);

    customInterceptor.setOrder(AuthorizationInterceptorsOrder.POST_AUTHORIZE.getOrder());

    return customInterceptor;
  }

  @Bean(HAS_AUTHORITY_ANNOTATION_POINTCUT)
  Pointcut hasAuthorityPointCut() {
    return Pointcuts.union(
        new AnnotationMatchingPointcut(null, HasAuthority.class, true),
        new AnnotationMatchingPointcut(HasAuthority.class, true));
  }
}
