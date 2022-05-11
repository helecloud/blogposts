package com.helecloud.blog.security;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class HasAuthorityAuthorizationManager implements AuthorizationManager<MethodInvocation> {

  private final AuthorizationAnnotationUtils annotationUtils;

  public HasAuthorityAuthorizationManager(final AuthorizationAnnotationUtils annotationUtils) {
    this.annotationUtils = annotationUtils;
  }

  @Override
  public AuthorizationDecision check(final Supplier<Authentication> authentication,
      final MethodInvocation object) {

    final Collection<String> grantedAuthorities =
        Optional.ofNullable(authentication)
            .map(Supplier::get)
            .map(Authentication::getAuthorities)
            .filter(Objects::nonNull)
            .orElse(Collections.emptySet())
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());

    final HasAuthority hasAuthorityAnnotation = findHasAuthorityAnnotation(object.getMethod());

    final boolean granted = grantAccess(grantedAuthorities, hasAuthorityAnnotation);

    return new AuthorizationDecision(granted);
  }

  private boolean grantAccess(final Collection<String> grantedAuthorities, final HasAuthority hasAuthority) {
    final Set<Authority> any = Set.of(hasAuthority.hasAny());
    for (final Authority authority : any) {
      if (grantedAuthorities.contains(authority.getValue())) {
        return true;
      }
    }

    final Set<String> all =
        Arrays.stream(hasAuthority.hasAll()).map(Authority::getValue).collect(Collectors.toSet());

    return !all.isEmpty() && grantedAuthorities.containsAll(all);
  }


  private HasAuthority findHasAuthorityAnnotation(final Method method) {
    final HasAuthority hasAuthority =
        annotationUtils.findUniqueAnnotation(method, HasAuthority.class);
    return hasAuthority != null
        ? hasAuthority
        : annotationUtils.findUniqueAnnotation(method.getDeclaringClass(), HasAuthority.class);
  }

}
