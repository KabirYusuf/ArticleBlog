package dev.levelupschool.backend.controller;

import dev.levelupschool.backend.data.dto.request.AuthenticationRequest;

import dev.levelupschool.backend.data.dto.request.RegistrationRequest;
import dev.levelupschool.backend.data.dto.request.UpdateUserRequest;
import dev.levelupschool.backend.data.dto.response.AuthenticationResponse;
import dev.levelupschool.backend.data.model.User;
import dev.levelupschool.backend.data.repository.UserRepository;
import dev.levelupschool.backend.service.auth.AuthenticationService;
import dev.levelupschool.backend.service.interfaces.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static dev.levelupschool.backend.util.Serializer.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Testcontainers
@ActiveProfiles("test-container")
@Sql(scripts = "classpath:reset-database.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationService authenticationService;
    private String authHeader;
    private RegistrationRequest registrationRequest;

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:16-alpine")
        .withDatabaseName("levelup");
    @BeforeAll
    static  void beforeAll(){
        postgreSQLContainer.start();
    }
    @AfterAll
    static void afterAll(){
        postgreSQLContainer.stop();
    }
    @Test
    void contextLoads() {
    }

    @BeforeEach
    void setUp(){
        userRepository.deleteAll();
        registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("kaybee");
        registrationRequest.setEmail("k@gmail.com");
        registrationRequest.setPassword("1234567");
        authenticationService.register(registrationRequest);
        AuthenticationRequest authenticationRequest;
        authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("kaybee");
        authenticationRequest.setPassword("1234567");

        User foundUser = userRepository.findById(1L).get();

        foundUser.setVerified(true);

        userRepository.save(foundUser);

        AuthenticationResponse authenticationResponse = authenticationService.login(authenticationRequest);

        authHeader = "Bearer " + authenticationResponse.getToken();
    }


    @Test
    public void givenAuthors_whenGetAuthors_thenReturnJsonArray() throws Exception {
        mvc.perform(
                get("/users")
                    .header("Authorization", authHeader)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.items", hasSize(1)));
    }

    @Test
    public void givenAuthor_whenGetAuthorWithId_thenReturnJsonOfThatSpecificAuthor() throws Exception {
        mvc.perform(
                get("/users/1")
                    .header("Authorization", authHeader)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName", nullValue()));
    }

    @Test
    public void givenUser_whenDeleteUserWithId_then200IsReturnedAsStatusCode() throws Exception {
        Assertions.assertEquals(1, userRepository.findAll().size());
       mvc.perform(delete("/users/1")
               .header("Authorization", authHeader)
           )
            .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals(0, userRepository.findAll().size());
    }

    @Test
    void givenUpdateUserRequest_whenUserIsUpdated_UserIsUpdatedInDatabase() throws Exception {
        Assertions.assertNull(userService.findUserById(1L).getFirstName());

        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setFirstName("Marko");
        updateUserRequest.setLastName("James");

       mvc.perform(
           put("/users/1")
               .header("Authorization", authHeader)
               .contentType(MediaType.APPLICATION_JSON)
               .content(asJsonString(updateUserRequest))
       )
           .andExpect(status().isOk());
       Assertions.assertEquals("Marko", userService.findUserById(1L).getFirstName());
    }

}
