package src.main.java.crm;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import org.springframework.context.annotation.ComponentScan;



@EnableAutoConfiguration
@ComponentScan("src.main.java.crm")
public class MainClass {

    public static void main(String[] args) throws Exception {

        SpringApplication.run(MainClass.class, args);

    }

}