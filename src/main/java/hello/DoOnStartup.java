package src.main.java.hello;

import org.springframework.boot.CommandLineRunner;
// https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-command-line-runner

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class DoOnStartup implements CommandLineRunner {

    @Autowired
    AccountRepository accountRepository;

    public void run(String[] args) {

        Account account;

        account = accountRepository.findByUsername("TheOne");
        if (account==null)
            accountRepository.save(new Account("TheOne", "Pcw", true, "ROLE_DBA"));

        account = accountRepository.findByUsername("URW");
        if (account==null)
            accountRepository.save(new Account("URW", "Pcw", true, "ROLE_USER_READ_WRITE"));

        account = accountRepository.findByUsername("UR");
        if (account==null)
            accountRepository.save(new Account("UR", "Pcw", true, "ROLE_USER_READ"));

        System.out.println("Started Successfully, check http://localhost:8080/testdata \n\n\n\n");
    }

}