package me.chandler.springbootbasic.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("/요청시 Hello World 출력")
    public void print_Hello_World() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World"))
                .andDo(print());
    }

    @Test
    @DisplayName("계정 등록 - query parameter")
    public void signUp_account() throws Exception {
        mockMvc.perform(post("/account")
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("name", "memberA")
                        .param("age", "20"))
                .andExpect(status().isOk())
                .andExpect(content().string("signUp account"))
                .andDo(print());
    }

    @Test
    @DisplayName("계정 등록 - JSON")
    public void signUp_json() throws Exception {
        mockMvc.perform(post("/account")
                        .contentType(APPLICATION_JSON)
                        .content("{\"name\":\"memberA\", \"age\":\"20\"}"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("memberA"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value("20"))
                .andDo(print());
    }

}