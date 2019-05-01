package com.quartet.inventorydemo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

public interface DetailsService extends UserDetailsService {

    UserDetails loadUserByUsername(@NotBlank @Valid String username);
}