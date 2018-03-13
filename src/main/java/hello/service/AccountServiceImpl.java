package src.main.java.hello;

import src.main.java.hello.exceptions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;

@Service("accountService")
@Repository
@Transactional
public class AccountServiceImpl implements  AccountService {

    @Autowired
    private AccountRepository accountRepository;


    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public void delete(Account account) {
        accountRepository.delete(account);
    }

    @Override
    @Transactional(readOnly = true)
    public  Account findById(Long id) {
        return accountRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Account findByUsername(String username){return accountRepository.findByUsername(username);}

    @Override
    @Transactional(readOnly = true)
    public Page<Account> findAllByPage(Integer page, Integer limit) {
        if ( page == null || limit == null || !(page>0 && limit>0) )
            throw new InternalServerErrorException("INTERNAL_SERVER_ERROR");


        return accountRepository.findAllByOrderByIdAsc(new PageRequest(page-1, limit));
    }

}