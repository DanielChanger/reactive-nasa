package com.changer.reactivenasa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Hooks;

@SpringBootApplication
public class ReactiveNasaApplication {

    public static void main(String[] args) {
        Hooks.onOperatorDebug();
        SpringApplication.run(ReactiveNasaApplication.class, args);
    }

}
