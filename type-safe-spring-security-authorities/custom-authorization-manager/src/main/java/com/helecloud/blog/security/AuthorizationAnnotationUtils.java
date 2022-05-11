package com.helecloud.blog.security;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.springframework.core.annotation.AnnotationConfigurationException;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * A wrapper around {@link AnnotationUtils} that checks for, and errors on, conflicting annotations.
 * This is specifically important for Spring Security annotations which are not designed to be
 * repeatable.
 *
 * <p>There are numerous ways that two annotations of the same type may be attached to the same
 * method. For example, a class may implement a method defined in two separate interfaces. If both
 * of those interfaces have a `@PreAuthorize` annotation, then it's unclear which `@PreAuthorize`
 * expression Spring Security should use.
 *
 * <p>Another way is when one of Spring Security's annotations is used as a meta-annotation. In that
 * case, two custom annotations can be declared, each with their own `@PreAuthorize` declaration. If
 * both custom annotations are used on the same method, then it's unclear which `@PreAuthorize`
 * expression Spring Security should use.
 *
 * @see <a
 *     href="https://raw.githubusercontent.com/spring-projects/spring-security/main/core/src/main/java/org/springframework/security/authorization/method/AuthorizationAnnotationUtils.java">AuthorizationAnnotationUtils</a>
 */
public interface AuthorizationAnnotationUtils {

  /**
   * Perform an exhaustive search on the type hierarchy of the given {@link Method} for the
   * annotation of type {@code annotationType}, including any annotations using {@code
   * annotationType} as a meta-annotation.
   *
   * <p>If more than one is found, then throw an error.
   *
   * @param method the method declaration to search from
   * @param annotationType the annotation type to search for
   * @return the unique instance of the annotation attributed to the method, {@code null} otherwise
   * @throws AnnotationConfigurationException if more than one instance of the annotation is found
   */
  <A extends Annotation> A findUniqueAnnotation(final Method method, final Class<A> annotationType);

  /**
   * Perform an exhaustive search on the type hierarchy of the given {@link Class} for the
   * annotation of type {@code annotationType}, including any annotations using {@code
   * annotationType} as a meta-annotation.
   *
   * <p>If more than one is found, then throw an error.
   *
   * @param type the type to search from
   * @param annotationType the annotation type to search for
   * @return the unique instance of the annotation attributed to the method, {@code null} otherwise
   * @throws AnnotationConfigurationException if more than one instance of the annotation is found
   */
  <A extends Annotation> A findUniqueAnnotation(final Class<?> type, final Class<A> annotationType);
}
