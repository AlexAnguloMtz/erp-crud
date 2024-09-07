package com.aram.erpcrud.users.integration;

import com.aram.erpcrud.auth.config.JwtHandler;
import com.aram.erpcrud.auth.domain.AuthRole;
import com.aram.erpcrud.auth.domain.AuthRoleRepository;
import com.aram.erpcrud.auth.domain.AuthUser;
import com.aram.erpcrud.auth.domain.AuthUserRepository;
import com.aram.erpcrud.auth.payload.LoginResponse;
import com.aram.erpcrud.auth.payload.LoginCommand;
import com.aram.erpcrud.users.domain.PersonalDetails;
import com.aram.erpcrud.users.domain.PersonalDetailsRepository;
import com.aram.erpcrud.users.payload.CreateUserCommand;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CreateUserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtHandler jwtHandler;

    @Autowired
    private AuthRoleRepository authRoleRepository;

    @Autowired
    private AuthUserRepository authUserRepository;

    @Autowired
    private PersonalDetailsRepository personalDetailsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${config.root-user.email}")
    private String rootUserEmail;

    @Value("${config.root-user.password}")
    private String rootUserPassword;

    private ObjectMapper mapper;

    private Faker faker;

    private String rootUserAccessToken;

    private AuthRole userRole;

    @BeforeEach
    public void setUp() {
        mapper = new ObjectMapper();
        faker = new Faker();
        try {
            userRole = authRoleRepository.findByDescription("ROLE_USER").get();
            rootUserAccessToken = accessToken(rootUserCredentials());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void create_user_success() throws Exception {
        CreateUserCommand requestDto = userCreationDto(userRole.getId());

        createUser(requestDto, HttpStatus.CREATED);

        Optional<AuthUser> userOptional = authUserRepository.findByEmail(requestDto.email());
        assertTrue(userOptional.isPresent());
        AuthUser authUser = userOptional.get();
        assertEquals(requestDto.roleId(), authUser.getRole().getId());
        assertTrue(passwordEncoder.matches(requestDto.password(), authUser.getPassword()));

        Optional<PersonalDetails> personalDetailsOptional = personalDetailsRepository.findByAccountId(authUser.getId());
        assertTrue(personalDetailsOptional.isPresent());
        compare(requestDto, personalDetailsOptional.get());
    }

    private void compare(CreateUserCommand dto, PersonalDetails personalDetails) {
        assertEquals(dto.name(), personalDetails.getName());
        assertEquals(dto.lastName(), personalDetails.getLastName());
        assertEquals(dto.state(), personalDetails.getState());
        assertEquals(dto.city(), personalDetails.getCity());
        assertEquals(dto.district(), personalDetails.getDistrict());
        assertEquals(dto.streetNumber(), personalDetails.getStreetNumber());
        assertEquals(dto.phone(), personalDetails.getPhone());
        assertEquals(dto.zipCode(), personalDetails.getZipCode());
    }

    private ResultActions createUser(CreateUserCommand dto, HttpStatus expectedStatus) throws Exception {
        return mockMvc.perform(post("/api/v1/users")
            .header("Authorization", "Bearer " + rootUserAccessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJson(dto)))
            .andExpect(status().is(expectedStatus.value()));
    }

    private CreateUserCommand userCreationDto(String roleId) {
        return new CreateUserCommand(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.address().state(),
                faker.address().city(),
                faker.address().secondaryAddress(),
                faker.address().streetName(),
                faker.address().buildingNumber(),
                faker.address().zipCode(),
                faker.internet().emailAddress(),
                faker.phoneNumber().phoneNumber(),
                roleId,
                faker.internet().password()
        );
    }

    private LoginCommand rootUserCredentials() {
        return new LoginCommand(rootUserEmail, rootUserPassword);
    }

    private String accessToken(LoginCommand dto) throws Exception {
        String responseBody = login(dto, HttpStatus.OK);
        LoginResponse responseDto = fromJson(responseBody, LoginResponse.class);
        return responseDto.accessToken();
    }

    private String login(LoginCommand dto, HttpStatus expectedStatus) throws Exception {
        return mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(dto)))
                .andExpect(status().is(expectedStatus.value()))
                .andReturn()
                .getResponse().getContentAsString();
    }

    private <T> String toJson(T t) {
        try {
            return mapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T fromJson(String body, Class<T> clazz)  {
        try {
            return mapper.readValue(body, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}