package src.main.java.crm;

import org.springframework.data.domain.AuditorAware;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.Authentication;

public class AuditorAwareBeanImpl implements AuditorAware<String> {
    public String getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            return "Not Authenticated";
        else return ( (User) authentication.getPrincipal() ).getUsername();
        //Возвращаем залогиненного пользователя из SpringSecurity чтобы писать в LastModifiedBy / CreatedBy поля

    }
}
