package org.dyplom.aplikacja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@SpringBootApplication
public class AplikacjaDyplomApplication {

  public static void main(String[] args) {
    SpringApplication.run(AplikacjaDyplomApplication.class, args);
  }

}
