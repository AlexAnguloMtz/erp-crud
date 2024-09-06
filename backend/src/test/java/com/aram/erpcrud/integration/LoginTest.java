package com.aram.erpcrud.integration;

import com.aram.erpcrud.auth.config.JwtHandler;
import com.aram.erpcrud.auth.payload.LoginResponse;
import com.aram.erpcrud.auth.payload.LoginCommand;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtHandler jwtHandler;

    @Value("${config.root-user.email}")
    private String rootUserEmail;

    @Value("${config.root-user.password}")
    private String rootUserPassword;

    private ObjectMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    public void root_user_login_success() throws Exception {
        LoginCommand requestDto = new LoginCommand(rootUserEmail, rootUserPassword);

        String responseBody = login(requestDto, HttpStatus.OK);
        LoginResponse responseDto = fromJson(responseBody, LoginResponse.class);
        Optional<String> subject = jwtHandler.parseSubject(responseDto.accessToken());

        assertTrue(jwtHandler.isValidToken(responseDto.accessToken()));
        assertTrue(subject.isPresent());
        assertEquals(subject.get(), requestDto.email());
    }

    private String login(LoginCommand dto, HttpStatus expectedStatus) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJson(dto)))
            .andExpect(status().is(expectedStatus.value()))
            .andReturn()
            .getResponse().getContentAsString();
    }

    private <T> String toJson(T t) throws JsonProcessingException {
        return mapper.writeValueAsString(t);
    }

    private <T> T fromJson(String body, Class<T> clazz) throws JsonProcessingException {
        return mapper.readValue(body, clazz);
    }
}