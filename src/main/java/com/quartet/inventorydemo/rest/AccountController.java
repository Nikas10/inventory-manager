package com.quartet.inventorydemo.rest;

import com.quartet.inventorydemo.model.Account;
import com.quartet.inventorydemo.service.AccountService;
import com.quartet.inventorydemo.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/account")
public class AccountController {

    @Autowired
    @Qualifier("AccountService")
    private AccountService accSrv;

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
    public ResponseEntity<?> getAccount(@PathVariable("login") String login) {
        Account acc = accSrv.getByLogin(login);
        if (acc==null)
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST,"No user found");
        return Response.createResponse(acc);
    }

    /**
     * User's method for getting data about self
     *
     * @param principal Spring security class, containing user's login and pass via TOKEN parsing
     * @return ResponceEntity containing user Account Entity
     */
    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getAccount(Principal principal) {
        if(principal == null)
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Wrong or empty access token");
        Account acc = accSrv.getByLogin(principal.getName());
        return Response.createResponse(acc);
    }


    /**
     * Account find by email method
     *
     * @param mail user's email
     * @return user's account
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "find_user/mail/{mail}", method = RequestMethod.GET)
    public ResponseEntity<?> getAccountByMail(@PathVariable("mail") String mail) {
        String email = mail.replace('+','.');
        if (email.equals(""))
        {
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST,"Empty email");
        }
        Account res = accSrv.getByEmail(email);
        if (res==null)
        {
            return Response.createErrorResponse(HttpStatus.NO_CONTENT,"Not found");
        }
        return Response.createResponse(res);
    }



    /**
     * User register method
     * Admin Accounts is not available to register here
     *
     * @param account
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> registerNewAccount(@RequestBody Account account) {
        //acc check logic here
        String login  = account.getLogin();
        String email = account.getEmail();

        if (login.isEmpty() || (email.isEmpty()) || (account.getPass().isEmpty()))
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Invalid login, mail, or password.");
        Account check = accSrv.getByLogin(login);

        if (check!=null)
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Login already in use.");
        check = accSrv.getByEmail(email);

        if (check!=null)
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "E-mail already in use.");

        //acc manage logic:
        account.setRole("user");
        account.setPass(passwordEncoder.encode(account.getPass()));
        accSrv.add(account); //flush empty links object, receive new one
        return Response.createResponse(HttpStatus.OK);
    }

    /**
     * Admin register method
     * Admin Accounts are available to register here
     *
     * @param account
     * @return
     */
    @RequestMapping(value = "/admin/register", method = RequestMethod.POST)
    public ResponseEntity<?> registerAdmin(@RequestBody Account account) {
        //acc check logic here
        String login  = account.getLogin();
        String email = account.getEmail();

        if (login.isEmpty() || (email.isEmpty()) || (account.getPass().isEmpty()))
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Invalid login, mail, or password.");
        Account check = accSrv.getByLogin(login);

        if (check!=null)
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Login already in use.");
        check = accSrv.getByEmail(email);

        if (check!=null)
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "E-mail already in use.");
        //acc manage logic:
        account.setRole("admin");
        account.setPass(passwordEncoder.encode(account.getPass()));
        accSrv.add(account); //flush empty links object, receive new one
        return Response.createResponse(HttpStatus.OK);
    }

    /**
     * Staff register method
     * Staff Accounts are available to register here
     *
     * @param account
     * @return
     */
    @PreAuthorize("hasAuthority('STAFF')")
    @RequestMapping(value = "/staff/register", method = RequestMethod.POST)
    public ResponseEntity<?> registerStaff(@RequestBody Account account) {
        //acc check logic here
        String login  = account.getLogin();
        String email = account.getEmail();

        if (login.isEmpty() || (email.isEmpty()) || (account.getPass().isEmpty()))
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Invalid login, mail, or password.");
        Account check = accSrv.getByLogin(login);

        if (check!=null)
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "Login already in use.");
        check = accSrv.getByEmail(email);

        if (check!=null)
            return Response.createErrorResponse(HttpStatus.BAD_REQUEST, "E-mail already in use.");
        //acc manage logic:
        account.setRole("staff");
        account.setPass(passwordEncoder.encode(account.getPass()));
        accSrv.add(account); //flush empty links object, receive new one
        return Response.createResponse(HttpStatus.OK);
    }

}