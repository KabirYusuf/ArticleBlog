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
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    public void givenUpdateUserRequest_whenUserIsUpdated_UserIsUpdatedInDatabase() throws Exception {
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

    @Test
    public void givenUserAAndUserB_whenUserAFollowsUserB_userBGetsAdditionalFollowerAndUserASizeOfUsersFollowedIncreasesByOne() throws Exception {
        RegistrationRequest userARequest = new RegistrationRequest();
        userARequest.setUsername("kaybee1");
        userARequest.setEmail("sonkaybee@gmail.com");
        userARequest.setPassword("1234");
        AuthenticationResponse authenticationResponse = authenticationService.register(userARequest);

        String userAAuthHeader = "Bearer " + authenticationResponse.getToken();

        RegistrationRequest userBRequest = new RegistrationRequest();
        userBRequest.setUsername("kaybee2");
        userBRequest.setEmail("sonkaybee@gmail.com");
        userBRequest.setPassword("1234");

        authenticationService.register(userBRequest);

        User userABeforeFollowingUserB = userRepository.findByIdWithFollowing(2L).get();
        User userBBeforeUserAFollowed = userRepository.findByIdWithFollowers(3L).get();

        int numberOfUsersUserAIsFollowingBeforeFollowingUserB = userABeforeFollowingUserB.getFollowing().size();
        int numberOfUsersThatIsFollowingUserBBeforeUserAFollowsUserB = userBBeforeUserAFollowed.getFollowers().size();

        assertEquals(0, numberOfUsersThatIsFollowingUserBBeforeUserAFollowsUserB);
        assertEquals(0, numberOfUsersUserAIsFollowingBeforeFollowingUserB);

        mvc.perform(
                post("/users/3/follows")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", userAAuthHeader)

            )
            .andExpect(status().isOk());

        User userAAfterFollowingUserB = userRepository.findByIdWithFollowing(2L).get();
        User userBAfterFollowedByUserA = userRepository.findByIdWithFollowers(3L).get();

        int numberOfUsersUserAIsFollowingAfterFollowingUserB = userAAfterFollowingUserB.getFollowing().size();
        int numberOfUsersThatIsFollowingUserBAfterUserAFollowsUserB = userBAfterFollowedByUserA.getFollowers().size();

        assertEquals(1, numberOfUsersThatIsFollowingUserBAfterUserAFollowsUserB);
        assertEquals(1, numberOfUsersUserAIsFollowingAfterFollowingUserB);
    }


    @Test
    public void givenUserAFollowsUserB_whenUserAUnfollowsUserB_userBNumberOfFollowersDecreasesByOneAndUserASizeOfUsersFollowedDecreasesByOne() throws Exception {
        RegistrationRequest userARequest = new RegistrationRequest();
        userARequest.setUsername("kaybee1");
        userARequest.setEmail("sonkaybee@gmail.com");
        userARequest.setPassword("1234");
        AuthenticationResponse authenticationResponse = authenticationService.register(userARequest);

        String userAAuthHeader = "Bearer " + authenticationResponse.getToken();

        RegistrationRequest userBRequest = new RegistrationRequest();
        userBRequest.setUsername("kaybee2");
        userBRequest.setEmail("sonkaybee@gmail.com");
        userBRequest.setPassword("1234");

        authenticationService.register(userBRequest);

        User userABeforeFollowingUserB = userRepository.findByIdWithFollowing(2L).get();
        User userBBeforeUserAFollowed = userRepository.findByIdWithFollowers(3L).get();

        int numberOfUsersUserAIsFollowingBeforeFollowingUserB = userABeforeFollowingUserB.getFollowing().size();
        int numberOfUsersThatIsFollowingUserBBeforeUserAFollowsUserB = userBBeforeUserAFollowed.getFollowers().size();

        assertEquals(0, numberOfUsersThatIsFollowingUserBBeforeUserAFollowsUserB);
        assertEquals(0, numberOfUsersUserAIsFollowingBeforeFollowingUserB);

        userService.followUser(userAAuthHeader, 3L);

        User userAAfterFollowingUserB = userRepository.findByIdWithFollowing(2L).get();
        User userBAfterFollowedByUserA = userRepository.findByIdWithFollowers(3L).get();

        int numberOfUsersUserAIsFollowingAfterFollowingUserB = userAAfterFollowingUserB.getFollowing().size();
        int numberOfUsersThatIsFollowingUserBAfterUserAFollowsUserB = userBAfterFollowedByUserA.getFollowers().size();

        assertEquals(1, numberOfUsersThatIsFollowingUserBAfterUserAFollowsUserB);
        assertEquals(1, numberOfUsersUserAIsFollowingAfterFollowingUserB);

        mvc.perform(
                delete("/users/3/follows")
                    .header("Authorization", userAAuthHeader)
            )
            .andExpect(status().isOk());

        User userAAfterUnfollowingUserB = userRepository.findByIdWithFollowing(2L).get();
        User userBAfterUserAUnfollowed = userRepository.findByIdWithFollowers(3L).get();

        int numberOfUsersUserAIsFollowingAfterUnfollowingUserB = userAAfterUnfollowingUserB.getFollowing().size();
        int numberOfUsersThatIsFollowingUserBAfterUserAUnfollowsUserB = userBAfterUserAUnfollowed.getFollowers().size();

        assertEquals(0, numberOfUsersThatIsFollowingUserBAfterUserAUnfollowsUserB);
        assertEquals(0, numberOfUsersUserAIsFollowingAfterUnfollowingUserB);
    }
    @Test
    public void givenUserAFollowsUserB_whenUserBFollowers_jsonArrayIsReturned() throws Exception {
        RegistrationRequest userARequest = new RegistrationRequest();
        userARequest.setUsername("kaybee1");
        userARequest.setEmail("sonkaybee@gmail.com");
        userARequest.setPassword("1234");
        AuthenticationResponse authenticationResponse = authenticationService.register(userARequest);

        String userAAuthHeader = "Bearer " + authenticationResponse.getToken();

        RegistrationRequest userBRequest = new RegistrationRequest();
        userBRequest.setUsername("kaybee2");
        userBRequest.setEmail("sonkaybee@gmail.com");
        userBRequest.setPassword("1234");

        AuthenticationResponse userBAuthenticationResponse = authenticationService.register(userBRequest);
        String userBAuthHeader = "Bearer " + userBAuthenticationResponse.getToken();
        userService.followUser(userAAuthHeader, 3L);
        mvc.perform(
            get("/users/followers")
                .header("Authorization", userBAuthHeader)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.*", hasSize(1)))
            .andExpect(jsonPath("$[0].username", is("kaybee1")));
    }
    @Test
    public void givenUserAFollowsUserB_whenUserAFollowing_jsonArrayIsReturned() throws Exception {
        RegistrationRequest userARequest = new RegistrationRequest();
        userARequest.setUsername("kaybee1");
        userARequest.setEmail("sonkaybee@gmail.com");
        userARequest.setPassword("1234");
        AuthenticationResponse authenticationResponse = authenticationService.register(userARequest);

        String userAAuthHeader = "Bearer " + authenticationResponse.getToken();

        RegistrationRequest userBRequest = new RegistrationRequest();
        userBRequest.setUsername("kaybee2");
        userBRequest.setEmail("sonkaybee@gmail.com");
        userBRequest.setPassword("1234");

        AuthenticationResponse userBAuthenticationResponse = authenticationService.register(userBRequest);
        String userBAuthHeader = "Bearer " + userBAuthenticationResponse.getToken();
        userService.followUser(userAAuthHeader, 3L);
        mvc.perform(
                get("/users/following")
                    .header("Authorization", userAAuthHeader)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.*", hasSize(1)))
            .andExpect(jsonPath("$[0].username", is("kaybee2")));
    }
}
