package dev.levelupschool.backend.controller;

import dev.levelupschool.backend.data.dto.request.AuthenticationRequest;

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

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:latest")
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
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("kabir@gmail.com");
        authenticationRequest.setPassword("12345");
        authenticationService.register(authenticationRequest);

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
    public void givenAuthor_whenDeleteAuthorWithId_then200IsReturnedAsStatusCode() throws Exception {
        Assertions.assertEquals(1, userRepository.findAll().size());
       mvc.perform(delete("/users/1")
               .header("Authorization", authHeader)
           )
            .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals(0, userRepository.findAll().size());
    }

    @Test
    void givenUpdateAuthorRequest_whenAuthorIsUpdated_authorIsUpdatedInDatabase() throws Exception {
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
