package src.main.java.crm;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.AuthorityUtils;

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    AccountRepository accountRepository;


    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl());
    }

    @Bean
    UserDetailsService userDetailsServiceImpl() {
        return new UserDetailsService() {

            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                //System.out.println(username);
                Account account = accountRepository.findByUsername(username);
                //System.out.println(account.getRole());
                if (account == null) throw new UsernameNotFoundException("NOT_FOUND");

                return new User(account.getUsername(), account.getPassword(), account.getIsEnabled(), true, true, true,
                        AuthorityUtils.createAuthorityList(account.getRole()));
                //https://docs.spring.io/autorepo/docs/spring-security/4.2.x-SNAPSHOT/apidocs/org/springframework/security/core/userdetails/User.html


            }
        };

    }
}
