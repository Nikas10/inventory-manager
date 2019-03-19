package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.model.Account;
import com.quartet.inventorydemo.repository.AccountRepository;
import com.quartet.inventorydemo.service.DetailsService;
import org.hibernate.exception.LockAcquisitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;

@Service("DetailsService")
@Transactional(rollbackFor = LockAcquisitionException.class)

public class DetailsServiceImpl implements DetailsService {

    private final AccountRepository accountRepository;

    @Autowired
    public DetailsServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            username = URLDecoder.decode(username, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            //TODO: Write exception handler
        }
        Account account  = accountRepository.findByLogin(username);
        if (account!=null)
        {
            return new org.springframework.security.core.userdetails.User(account.getLogin(),
                    account.getPass(),
                    true, true, true, true,
                    getGrantedAuthorities(account));
        }
        else {
            throw new UsernameNotFoundException("User " + username + " not found in database.");
        }
    }

    private Collection<? extends GrantedAuthority> getGrantedAuthorities(Account user) {
        Collection<? extends GrantedAuthority> authorities;
        if (user.getAdmin()) {
            authorities = AuthorityUtils.createAuthorityList("ADMIN", "USER");
        } else {
            authorities = AuthorityUtils.createAuthorityList("USER");
        }
        return authorities;
    }
}