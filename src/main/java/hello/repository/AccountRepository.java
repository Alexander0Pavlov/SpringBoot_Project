package src.main.java.hello;

import org.springframework.data.repository.PagingAndSortingRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {

    public Account findByUsername(String username);

    public Page<Account> findAllByOrderByIdAsc(Pageable pageable);
}