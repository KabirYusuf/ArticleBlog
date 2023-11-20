package dev.levelupschool.backend.controller;

import dev.levelupschool.backend.data.dto.request.AuthenticationRequest;
import dev.levelupschool.backend.data.model.User;
import dev.levelupschool.backend.data.repository.UserRepository;
import dev.levelupschool.backend.service.auth.AuthenticationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static dev.levelupschool.backend.util.Serializer.asJsonString;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuthenticationControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private AuthenticationService authenticationService;
    private String authHeader;

    @Autowired
    private UserRepository userRepository;
    private AuthenticationRequest authenticationRequest;
    @Test
    void contextLoads() {
    }
    @BeforeEach
    void setUp() {
        authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("kabir@gmail.com");
        authenticationRequest.setPassword("12345");

    }

    @Test
    void testThatWhenAUserRegister_UserRecordIncreasesByOne() throws Exception {
        Assertions.assertEquals(0, userRepository.findAll().size());
        mvc.perform(
                post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(authenticationRequest))
            )
            .andExpect(status().isCreated());

        Assertions.assertEquals(1, userRepository.findAll().size());
    }

    @Test
    void testThatTokenIsReturnedWhenAUserLogsInWithValidCredentials() throws Exception {
        mvc.perform(
            post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(authenticationRequest))
        );

        User foundUser = userRepository.findById(1L).get();

        foundUser.setVerified(true);

        userRepository.save(foundUser);


        mvc.perform(
                post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(authenticationRequest))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token", notNullValue()));
    }

}
