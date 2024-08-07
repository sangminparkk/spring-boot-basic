package me.chandler.springbootbasic.controller;

import lombok.extern.slf4j.Slf4j;
import me.chandler.springbootbasic.account.Account;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class AccountController {

    @GetMapping("/")
    public String greeting() {
        return "Hello World";
    }

    @PostMapping("/account")
    public Map<String, String> signUp(@RequestBody Account account) {
        log.info("name={}, age={}", account.getName(), account.getAge());

        Map<String, String> response = new HashMap<>();
        response.put("name", account.getName());
        response.put("age", account.getAge());

        return response;
    }

}
