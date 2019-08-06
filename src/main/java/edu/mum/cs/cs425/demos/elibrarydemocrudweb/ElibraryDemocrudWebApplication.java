package edu.mum.cs.cs425.demos.elibrarydemocrudweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class ElibraryDemocrudWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElibraryDemocrudWebApplication.class, args);
    }
    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
