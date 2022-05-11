package com.helecloud.blog.security;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class HasAuthorityAspect {

  static final Supplier<Authentication> AUTHENTICATION_SUPPLIER =
      () -> {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
          throw new AuthenticationCredentialsNotFoundException(
              "An Authentication object was not found in the SecurityContext");
        }
        return authentication;
      };

  private final AuthorizationAnnotationUtils annotationUtils;

  public HasAuthorityAspect(
      AuthorizationAnnotationUtils annotationUtils) {
    this.annotationUtils = annotationUtils;
  }

  @Before("@annotation(com.helecloud.blog.security.HasAuthority)")
  public void hasAuthorityCheck(final JoinPoint joinPoint) throws Throwable {

    final Collection<? extends GrantedAuthority> authenticationAuthorities =
        Optional.ofNullable(AUTHENTICATION_SUPPLIER)
            .map(Supplier::get)
            .map(Authentication::getAuthorities)
            .filter(Objects::nonNull)
            .filter(authorities -> !authorities.isEmpty())
            .orElseThrow(() -> new AccessDeniedException("Access Denied: Unauthenticated"));

    final Set<String> grantedAuthorities =
        authenticationAuthorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());

    final Method method = MethodSignature.class.cast(joinPoint.getSignature()).getMethod();
    final HasAuthority hasAuthorityAnnotation = findHasAuthorityAnnotation(method);

    final Set<Authority> any = Set.of(hasAuthorityAnnotation.hasAny());
    for (final Authority authority : any) {
      if (grantedAuthorities.contains(authority.getValue())) {
        return;
      }
    }

    Set<String> allRequired = Stream.of(hasAuthorityAnnotation.hasAll())
        .map(Authority::getValue)
        .collect(Collectors.toSet());
    if (!allRequired.isEmpty() && grantedAuthorities.containsAll(allRequired)) {
      return;
    }

    throw new AccessDeniedException("Access Denied: Unauthorized");
  }

  private HasAuthority findHasAuthorityAnnotation(final Method method) {
    final HasAuthority hasAuthority =
        annotationUtils.findUniqueAnnotation(method, HasAuthority.class);
    return hasAuthority != null
        ? hasAuthority
        : annotationUtils.findUniqueAnnotation(method.getDeclaringClass(), HasAuthority.class);
  }
}
