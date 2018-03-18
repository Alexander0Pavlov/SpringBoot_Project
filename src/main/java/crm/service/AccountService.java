package src.main.java.crm;

import java.util.List;

import org.springframework.data.domain.Page;


import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize(HasRole.DBA)
public interface AccountService {

    public Account save(Account account);

    public void delete(Account account);

    public Account findById(Long id);

    public Account findByUsername(String username);

    public Page<Account> findAllByPage(Integer page, Integer limit);
}