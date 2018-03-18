package src.main.java.crm;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import java.util.Properties;

@Configuration
public class JavaMailSenderConfig {
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl(); //создали

        //настроили
        javaMailSenderImpl.setHost("smtp.gmail.com");
        javaMailSenderImpl.setPort(587);

        javaMailSenderImpl.setUsername("alexpavlovspring@gmail.com"); //здесь свой мейл
        javaMailSenderImpl.setPassword("**************************");  // здесь свой пароль


        Properties properties = javaMailSenderImpl.getJavaMailProperties(); //взяли ссылку на Properties и настроили
        //"Allow Map access to the JavaMail properties of this sender, with the option to add or override specific entries."
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.debug", "true");

        properties.setProperty("mail.smtp.allow8bitmime", "true");
        properties.setProperty("mail.smtps.allow8bitmime", "true");


        return javaMailSenderImpl; //вернули
    }

}