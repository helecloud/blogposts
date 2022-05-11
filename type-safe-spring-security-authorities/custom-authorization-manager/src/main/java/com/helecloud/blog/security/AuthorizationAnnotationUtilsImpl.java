package com.helecloud.blog.security;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.springframework.core.annotation.AnnotationConfigurationException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.annotation.RepeatableContainers;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationAnnotationUtilsImpl implements AuthorizationAnnotationUtils {

  @Override
  public <A extends Annotation> A findUniqueAnnotation(
      final Method method, final Class<A> annotationType) {
    final MergedAnnotations mergedAnnotations = MergedAnnotations.from(method,
        MergedAnnotations.SearchStrategy.TYPE_HIERARCHY, RepeatableContainers.none());
    if (hasDuplicate(mergedAnnotations, annotationType)) {
      throw new AnnotationConfigurationException("Found more than one annotation of type " + annotationType
          + " attributed to " + method
          + " Please remove the duplicate annotations and publish a bean to handle your authorization logic.");
    }
    return AnnotationUtils.findAnnotation(method, annotationType);
  }

  @Override
  public <A extends Annotation> A findUniqueAnnotation(
      final Class<?> type, final Class<A> annotationType) {
    final MergedAnnotations mergedAnnotations = MergedAnnotations.from(type,
        MergedAnnotations.SearchStrategy.TYPE_HIERARCHY, RepeatableContainers.none());
    if (hasDuplicate(mergedAnnotations, annotationType)) {
      throw new AnnotationConfigurationException("Found more than one annotation of type " + annotationType
          + " attributed to " + type
          + " Please remove the duplicate annotations and publish a bean to handle your authorization logic.");
    }
    return AnnotationUtils.findAnnotation(type, annotationType);
  }

  private static <A extends Annotation> boolean hasDuplicate(final MergedAnnotations mergedAnnotations,
      final Class<A> annotationType) {
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

  private AuthorizationAnnotationUtilsImpl() {}
}
