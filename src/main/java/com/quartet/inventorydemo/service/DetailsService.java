package com.quartet.inventorydemo.service;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface DetailsService extends UserDetailsService {

  UserDetails loadUserByUsername(@NotBlank @Valid String username);
}
