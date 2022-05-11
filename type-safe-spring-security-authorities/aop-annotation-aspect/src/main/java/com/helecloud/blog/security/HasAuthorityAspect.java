package com.helecloud.blog.security;

import java.lang.annotation.Annotation;
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
import org.springframework.core.annotation.AnnotationConfigurationException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.annotation.RepeatableContainers;
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

  // https://github.com/spring-projects/spring-security/blob/86c24da38b9b4452d2208fe4ea46e9c35e0d6f93/core/src/main/java/org/springframework/security/authorization/method/PreAuthorizeAuthorizationManager.java#L96
  private HasAuthority findHasAuthorityAnnotation(final Method method) {
    final HasAuthority hasAuthority =
        (HasAuthority) HasAuthorityAspect.findUniqueAnnotation(method, HasAuthority.class);
    return hasAuthority != null
        ? hasAuthority
        : (HasAuthority)
            HasAuthorityAspect.findUniqueAnnotation(method.getDeclaringClass(), HasAuthority.class);
  }

  // Copied from
  // https://github.com/spring-projects/spring-security/blob/67e5c05a47067b6a65d493529ffa485a26bec219/core/src/main/java/org/springframework/security/authorization/method/AuthorizationAnnotationUtils.java#L60

  static <A extends Annotation> A findUniqueAnnotation(
      final Method method, final Class<A> annotationType) {
    final MergedAnnotations mergedAnnotations =
        MergedAnnotations.from(
            method, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY, RepeatableContainers.none());
    if (hasDuplicate(mergedAnnotations, annotationType)) {
      throw new AnnotationConfigurationException(
          "Found more than one annotation of type "
              + annotationType
              + " attributed to "
              + method
              + " Please remove the duplicate annotations and publish a bean to handle your authorization logic.");
    }
    return AnnotationUtils.findAnnotation(method, annotationType);
  }

  static <A extends Annotation> A findUniqueAnnotation(
      final Class<?> type, final Class<A> annotationType) {
    final MergedAnnotations mergedAnnotations =
        MergedAnnotations.from(
            type, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY, RepeatableContainers.none());
    if (hasDuplicate(mergedAnnotations, annotationType)) {
      throw new AnnotationConfigurationException(
          "Found more than one annotation of type "
              + annotationType
              + " attributed to "
              + type
              + " Please remove the duplicate annotations and publish a bean to handle your authorization logic.");
    }
    return AnnotationUtils.findAnnotation(type, annotationType);
  }

  private static <A extends Annotation> boolean hasDuplicate(
      final MergedAnnotations mergedAnnotations, final Class<A> annotationType) {
    boolean alreadyFound = false;
    for (final MergedAnnotation<Annotation> mergedAnnotation : mergedAnnotations) {
      if (mergedAnnotation.getType() == annotationType) {
        if (alreadyFound) {
          return true;
        }
        alreadyFound = true;
      }
    }
    return false;
  }
}
