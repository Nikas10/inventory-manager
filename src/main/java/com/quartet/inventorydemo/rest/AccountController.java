package com.quartet.inventorydemo.rest;

import com.quartet.inventorydemo.dto.AccountDTO;
import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.Account;
import com.quartet.inventorydemo.service.AccountService;
import com.quartet.inventorydemo.util.UUIDString;
import java.security.Principal;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/accounts")
@Validated
public class AccountController {

  @Autowired
  @Qualifier("AccountService")
  private AccountService accountService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  /**
   * General Account get method for admin or worker
   *
   * @param login user's login
   * @return ResponceEntity containing user Account Entity, with password field clear
   */
  @PreAuthorize("hasAuthority('ADMIN')")
  @RequestMapping(value = "/{login}", method = RequestMethod.GET)
  public ResponseEntity<?> getAccount(@PathVariable("login") @NotBlank @Valid String login) {
    Optional<Account> accountOptional = accountService.getByLogin(login);
    accountOptional.orElseThrow(
        () -> new ResourceNotFoundException("Account with login: " + login + " not found"));
    return new ResponseEntity<>(accountOptional.get(), HttpStatus.OK);
  }

  /**
   * User's method for getting data about self
   *
   * @param principal Spring security class, containing user's login and pass via TOKEN parsing
   * @return ResponceEntity containing user Account Entity
   */
  @PreAuthorize("hasAuthority('USER')")
  @RequestMapping(value = "/me", method = RequestMethod.GET)
  public ResponseEntity<?> getAccount(@NotNull @Valid Principal principal) {
    Optional<Account> accountOptional = accountService.getByLogin(principal.getName());
    accountOptional.orElseThrow(
        () ->
            new ResourceNotFoundException(
                "Account with login: " + principal.getName() + " not found"));
    return new ResponseEntity<>(accountOptional.get(), HttpStatus.OK);
  }

  /**
   * Account find by email method
   *
   * @param email user's email
   * @return user's account
   */
  @PreAuthorize("hasAuthority('ADMIN')")
  @RequestMapping(value = "/mail/{mail}", method = RequestMethod.GET)
  public ResponseEntity<?> getAccountByMail(@PathVariable("mail") @Email @Valid String email) {
    Optional<Account> accountOptional = accountService.getByEmail(email);
    accountOptional.orElseThrow(
        () -> new ResourceNotFoundException("Account with email: " + email + " not found"));
    return new ResponseEntity<>(accountOptional.get(), HttpStatus.OK);
  }

  /**
   * User register method Admin Accounts is not available to register here
   */
  @RequestMapping(value = "/user", method = RequestMethod.POST)
  public ResponseEntity<?> registerNewAccount(@RequestBody Account account) {
    // acc manage logic:
    account.setRole("user");
    account.setPassword(passwordEncoder.encode(account.getPassword()));
    Account newAccount = accountService.add(account); // flush empty links object, receive new one
    return new ResponseEntity<>(newAccount, HttpStatus.OK);
  }

  /**
   * Admin register method Admin Accounts are available to register here
   */
  @RequestMapping(value = "/admin", method = RequestMethod.POST)
  public ResponseEntity<?> registerAdmin(@RequestBody Account account) {
    // acc manage logic:
    account.setRole("admin");
    account.setPassword(passwordEncoder.encode(account.getPassword()));
    Account newAccount = accountService.add(account); // flush empty links object, receive new one
    return new ResponseEntity<>(newAccount, HttpStatus.OK);
  }

  /**
   * Staff register method Staff Accounts are available to register here
   */
  @PreAuthorize("hasAuthority('STAFF')")
  @RequestMapping(value = "/staff", method = RequestMethod.POST)
  public ResponseEntity<?> registerStaff(@RequestBody Account account) {
    // acc manage logic:
    account.setRole("staff");
    account.setPassword(passwordEncoder.encode(account.getPassword()));
    Account newAccount = accountService.add(account); // flush empty links object, receive new one
    return new ResponseEntity<>(newAccount, HttpStatus.OK);
  }

  // @PreAuthorize("hasAuthority('STAFF')")
  @RequestMapping(value = "/{login}/holders/", method = RequestMethod.GET)
  public ResponseEntity<?> getAccountLinksToHolders(
      @PathVariable("login") @NotBlank @Valid String login) {
    Optional<Account> accountOptional = accountService.getByLogin(login);
    Account accountWithHolders =
        accountOptional.orElseThrow(
            () -> new ResourceNotFoundException("Account with login: " + login + " not found"));
    return new ResponseEntity<>(accountWithHolders.getHolders(), HttpStatus.OK);
  }

  // @PreAuthorize("hasAuthority('USER')")
  @RequestMapping(value = "/me/holders/", method = RequestMethod.GET)
  public ResponseEntity<?> getAccountLinksToHolders(@NotNull @Valid Principal principal) {
    Optional<Account> accountOptional = accountService.getByLogin(principal.getName());
    Account accountWithHolders =
        accountOptional.orElseThrow(
            () ->
                new ResourceNotFoundException(
                    "Account with login: " + principal.getName() + " not found"));
    return new ResponseEntity<>(accountWithHolders.getHolders(), HttpStatus.OK);
  }

  // @PreAuthorize("hasAuthority('STAFF')")
  @RequestMapping(value = "/{login}/holders", method = RequestMethod.POST)
  public ResponseEntity<?> addHolderLink(
      @PathVariable("login") @NotBlank @Valid String login,
      @RequestParam("holderId") @NotBlank @UUIDString String holderId) {
    UUID holder = UUID.fromString(holderId);
    accountService.addHolder(login, holder);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  // @PreAuthorize("hasAuthority('STAFF')")
  @RequestMapping(value = "/{login}/holders/{holderId}", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteHolderLink(
      @PathVariable("login") @NotBlank @Valid String login,
      @PathVariable("holderId") @NotBlank @UUIDString String holderId) {
    UUID holder = UUID.fromString(holderId);
    accountService.removeHolder(login, holder);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  // @PreAuthorize("hasAuthority('STAFF')")
  @RequestMapping(value = "/{login}/requisitions/", method = RequestMethod.GET)
  public ResponseEntity<?> getAccountLinksToRequisitions(
      @PathVariable("login") @NotBlank @Valid String login) {
    Optional<Account> accountOptional = accountService.getByLogin(login);
    Account accountWithRequisitions =
        accountOptional.orElseThrow(
            () -> new ResourceNotFoundException("Account with login: " + login + " not found"));
    return new ResponseEntity<>(accountWithRequisitions.getRequisitions(), HttpStatus.OK);
  }

  // @PreAuthorize("hasAuthority('USER')")
  @RequestMapping(value = "/me/requisitions/", method = RequestMethod.GET)
  public ResponseEntity<?> getAccountLinksToRequisitions(@NotNull @Valid Principal principal) {
    Optional<Account> accountOptional = accountService.getByLogin(principal.getName());
    Account accountWithRequisitions =
        accountOptional.orElseThrow(
            () ->
                new ResourceNotFoundException(
                    "Account with login: " + principal.getName() + " not found"));
    return new ResponseEntity<>(accountWithRequisitions.getRequisitions(), HttpStatus.OK);
  }

  @RequestMapping(value = "/{login}", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteAccount(
      @PathVariable("login") @Valid String login) {
    accountService.remove(login);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @RequestMapping(value = "/{login}", method = RequestMethod.PATCH)
  public ResponseEntity<?> updateAccount(
      @PathVariable("login") @Valid String login, @RequestBody AccountDTO accountDTO) {
    if (accountDTO.getPassword() != null) {
      accountDTO.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
    }
    accountService.update(login, accountDTO);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public ResponseEntity<?> getAllAccounts() {
    return new ResponseEntity<>(accountService.getAll(), HttpStatus.OK);
  }
}
