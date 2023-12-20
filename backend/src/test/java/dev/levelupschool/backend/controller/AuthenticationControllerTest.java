package dev.levelupschool.backend.controller;

import dev.levelupschool.backend.data.dto.request.AuthenticationRequest;
import dev.levelupschool.backend.data.dto.request.NotificationRequest;
import dev.levelupschool.backend.data.dto.request.RegistrationRequest;
import dev.levelupschool.backend.data.model.User;
import dev.levelupschool.backend.data.model.enums.Role;
import dev.levelupschool.backend.data.repository.UserRepository;
import dev.levelupschool.backend.service.auth.AuthenticationService;
import dev.levelupschool.backend.service.notification.LevelUpEmailSenderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Set;

import static dev.levelupschool.backend.util.Serializer.asJsonString;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
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
    private UserRepository userRepository;
    private AuthenticationRequest authenticationRequest;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private RegistrationRequest registrationRequest;
    @MockBean
    private LevelUpEmailSenderService levelUpEmailSenderService;
    @MockBean
    private AuthenticationManager authenticationManager;
    @Test
    void contextLoads() {
    }
    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        doNothing().when(levelUpEmailSenderService).sendEmailNotification(any(NotificationRequest.class));
        registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("kaybee");
        registrationRequest.setEmail("sonkaybee@gmail.com");
        registrationRequest.setPassword("a12345A45@");

        authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("kaybee");
        authenticationRequest.setPassword("a12345A45@");

    }

    @Test
    void testThatWhenAUserRegister_UserRecordIncreasesByOne() throws Exception {
        doNothing().when(levelUpEmailSenderService).sendEmailNotification(any(NotificationRequest.class));
        Assertions.assertEquals(0, userRepository.findAll().size());
        mvc.perform(
                post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(registrationRequest))
            )
            .andExpect(status().isCreated());

        Assertions.assertEquals(1, userRepository.findAll().size());
    }

    @Test
    void testThatTokenIsReturnedWhenAUserLogsInWithValidCredentials() throws Exception {

        User user = new User();
        user.setUsername("kaybee");
        user.setPassword(passwordEncoder.encode("a12345A45@"));
        user.setRoles(Set.of(Role.USER));
        user.setVerified(true);
        userRepository.save(user);

        mvc.perform(
                post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(authenticationRequest))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token", notNullValue()));
    }

}
