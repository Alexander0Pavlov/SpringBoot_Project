package src.main.java.crm;

import src.main.java.crm.exceptions.BadRequestException;
import src.main.java.crm.exceptions.NotFoundException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.data.domain.Page;



@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    //curl -X GET -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -H 'Authorization: Basic VGhlT25lOlBjdw==' -i 'http://localhost:8080/account/find/all?p=1&l=15'
    @RequestMapping(value = "/account/find/all", method = RequestMethod.GET,
            consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public Page<Account> findAllByPage(
            @RequestParam(value = "p", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "l", required = false, defaultValue = "10") Integer limit) {
        if (!(page > 0 && limit > 0)) throw new BadRequestException("BAD_REQUEST");

        return accountService.findAllByPage(page, limit);
    }

    //curl -X POST -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -H 'Authorization: Basic VGhlT25lOlBjdw==' -i http://localhost:8080/account/ --data '{"username":"UR_New","password":"Pcw","isEnabled":true,"role":"ROLE_USER_READ"}'
    @RequestMapping(value = "/account", method = RequestMethod.POST, consumes = "application/json; charset=UTF-8",
            produces = "application/json; charset=UTF-8")
    @ResponseBody
    public Account create(@RequestBody @Valid Account account) {

        if (!(Lists.newArrayList(HasRole.ALL_ROLE_NAMES).contains(account.getRole())))
            throw new BadRequestException("BAD_REQUEST");
        if (accountService.findByUsername(account.getUsername()) != null)
            throw new BadRequestException("BAD_REQUEST"); //Already Exists

        accountService.save(account);

        return account;
    }

    //curl -X PUT -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -H 'Authorization: Basic VGhlT25lOlBjdw==' -i http://localhost:8080/account/6 --data '{"id":6,"username":"URW6","password":"Pcw","isEnabled":true,"role":"ROLE_USER_READ_WRITE"}'
    @RequestMapping(value = "/account/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Account update(@RequestBody @Valid Account account, @PathVariable Long id) {

        if (!(Lists.newArrayList(HasRole.ALL_ROLE_NAMES).contains(account.getRole())))
            throw new BadRequestException("BAD_REQUEST");

        Account accountWithThisUsername = accountService.findByUsername(account.getUsername());
        if (accountWithThisUsername != null && accountWithThisUsername.getId() != id)
            throw new BadRequestException("BAD_REQUEST"); //Already Exists под другим id

        Account accountSave = accountService.findById(id);
        if (accountSave == null) throw new NotFoundException("NOT_FOUND");

        // с @Valid проверки на нулл полей account делает валидатор
        accountSave.setUsername(account.getUsername());
        accountSave.setPassword(account.getPassword());
        accountSave.setIsEnabled(account.getIsEnabled());
        accountSave.setRole(account.getRole());

        accountService.save(accountSave);

        return accountSave;
    }


    //curl -X GET -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -H 'Authorization: Basic VGhlT25lOlBjdw==' -i http://localhost:8080/account/1
    @RequestMapping(value = "/account/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Account findAccountById(@PathVariable Long id) throws NotFoundException {

        Account account = accountService.findById(id);
        if (account == null) throw new NotFoundException("NOT_FOUND");

        return account;
    }


    //curl -X DELETE -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -H 'Authorization: Basic VGhlT25lOlBjdw==' -i http://localhost:8080/account/3
    @RequestMapping(value = "/account/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(@PathVariable Long id) throws NotFoundException {

        Account account = accountService.findById(id);

        if (account == null)
            throw new NotFoundException("NOT_FOUND");
        else accountService.delete(account);

    }

}