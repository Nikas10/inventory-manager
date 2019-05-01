package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.model.Account;
import com.quartet.inventorydemo.repository.AccountRepository;
import com.quartet.inventorydemo.service.DetailsService;
import org.hibernate.exception.LockAcquisitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Optional;

@Service("DetailsService")
@Transactional(rollbackFor = LockAcquisitionException.class)
public class DetailsServiceImpl implements DetailsService {
    private final AccountRepository accountRepository;

    @Autowired
    public DetailsServiceImpl(@Qualifier("AccountRepository") final AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            String decodedUsername = URLDecoder.decode(username, "UTF-8");
            Optional<Account> byLogin = accountRepository.findByLogin(username);

            byLogin.orElseThrow(() -> new UsernameNotFoundException("User " + decodedUsername + " not found in database."));
            Account account = byLogin.get();
            return new org.springframework.security.core.userdetails.User(account.getLogin(),
                    account.getPassword(),
                    true, true, true, true,
                    getGrantedAuthorities(account));
        } catch (UnsupportedEncodingException e) {
            //TODO log
            return null;
        }

    }

    private Collection<? extends GrantedAuthority> getGrantedAuthorities(Account user) {
        Collection<? extends GrantedAuthority> authorities;
        switch (user.getRole().toUpperCase()) {
            case "ADMIN":
                return AuthorityUtils.createAuthorityList("ADMIN", "USER", "STAFF");
            case "STAFF":
                return AuthorityUtils.createAuthorityList("USER", "STAFF");
            default:
                return AuthorityUtils.createAuthorityList("USER");
        }
    }
}