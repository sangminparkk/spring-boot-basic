package me.chandler.springbootbasic.controller;

import lombok.extern.slf4j.Slf4j;
import me.chandler.springbootbasic.account.Account;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AccountController {

    @GetMapping("/")
    public String greeting() {
        return "Hello World";
    }

    @PostMapping("/account")
    public String signUp(Account account) {
        log.info("name={}, age={}", account.getName(), account.getAge());
        return "signUp account";
    }

}
