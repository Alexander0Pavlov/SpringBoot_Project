package src.main.java.hello;

import org.springframework.data.domain.AuditorAware;

public class AuditorAwareBeanImpl implements AuditorAware<String> {
    public String getCurrentAuditor() {
        return "CREATED/MODIFIED BY USERNAME";
    }
}

// Можно использовать Spring Security и возвращать залогиненного пользователя
// return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()
