package com.quartet.inventorydemo.configuration.persistence;

import java.security.Principal;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class AuditorAwareImpl implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {
    if (SecurityContextHolder.getContext() != null
        && SecurityContextHolder.getContext().getAuthentication() != null
        && !SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
        && SecurityContextHolder.getContext().getAuthentication()
            instanceof AnonymousAuthenticationToken) {

      if (SecurityContextHolder.getContext()
          .getAuthentication()
          .getClass()
          .isAssignableFrom(OAuth2Authentication.class)) {
        OAuth2Authentication auth =
            (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        Principal principal = (Principal) auth.getUserAuthentication().getPrincipal();
        return Optional.of(principal.getName());
      }
    }
    return Optional.of("Unknown");
  }
}
