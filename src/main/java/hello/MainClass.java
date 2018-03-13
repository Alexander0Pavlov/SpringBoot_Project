package src.main.java.hello;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import org.springframework.context.annotation.ComponentScan;



@EnableAutoConfiguration
@ComponentScan("src.main.java.hello")
public class MainClass {

    public static void main(String[] args) throws Exception {

        SpringApplication.run(MainClass.class, args);

    }

}