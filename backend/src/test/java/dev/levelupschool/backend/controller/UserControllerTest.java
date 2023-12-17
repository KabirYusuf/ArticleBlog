package dev.levelupschool.backend.controller;

import dev.levelupschool.backend.data.dto.request.AuthenticationRequest;

import dev.levelupschool.backend.data.dto.request.PaymentDetails;
import dev.levelupschool.backend.data.dto.request.RegistrationRequest;
import dev.levelupschool.backend.data.dto.request.UpdateUserRequest;
import dev.levelupschool.backend.data.dto.response.AuthenticationResponse;
import dev.levelupschool.backend.data.dto.response.UserDTO;
import dev.levelupschool.backend.data.model.Article;
import dev.levelupschool.backend.data.model.User;
import dev.levelupschool.backend.data.model.enums.Role;
import dev.levelupschool.backend.data.repository.ArticleRepository;
import dev.levelupschool.backend.data.repository.UserRepository;
import dev.levelupschool.backend.exception.GeoException;
import dev.levelupschool.backend.security.JwtAuthenticationFilter;
import dev.levelupschool.backend.security.JwtService;
import dev.levelupschool.backend.security.SecuredUser;
import dev.levelupschool.backend.service.auth.AuthenticationService;
import dev.levelupschool.backend.service.interfaces.GeoService;
import dev.levelupschool.backend.service.interfaces.PaymentService;
import dev.levelupschool.backend.service.interfaces.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static dev.levelupschool.backend.util.Serializer.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
@Slf4j

//@Testcontainers
//@ActiveProfiles("test-container")
//@Sql(scripts = "classpath:reset-database.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserController userController;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private ArticleRepository articleRepository;
    private String authHeader;
    private RegistrationRequest registrationRequest;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private PaymentService paymentService;
    @MockBean
    private GeoService geoService;

//    @Container
//    @ServiceConnection
//    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:16-alpine")
//        .withDatabaseName("levelup");
//    @BeforeAll
//    static  void beforeAll(){
//        postgreSQLContainer.start();
//    }
//    @AfterAll
//    static void afterAll(){
//        postgreSQLContainer.stop();
//    }

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
        userService.registerUser(registrationRequest);

        when(jwtService.extractUsername("token")).thenReturn("kaybee");
    }


    @Test
    public void givenAuthors_whenGetAuthors_thenReturnJsonArray() throws Exception {
        mvc.perform(
                get("/users")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.items").exists());
    }

    @Test
    public void givenAuthor_whenGetAuthorWithId_thenReturnJsonOfThatSpecificAuthor() throws Exception {
        mvc.perform(
                get("/users/1")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName", nullValue()));
    }

    @Test
    public void givenUser_whenDeleteUserWithId_then200IsReturnedAsStatusCode() throws Exception {
        Assertions.assertEquals(1, userRepository.findAll().size());
        mvc.perform(delete("/users/1")
                .header("Authorization", "Bearer token")
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
                    .header("Authorization", "Bearer token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(updateUserRequest))
            )
            .andExpect(status().isOk());
        Assertions.assertEquals("Marko", userService.findUserById(1L).getFirstName());
    }

    @Test
    @Transactional
    public void givenUserAAndUserB_whenUserAFollowsUserB_userBGetsAdditionalFollowerAndUserASizeOfUsersFollowedIncreasesByOne() throws Exception {

        UserService mockUserService = mock(UserService.class);

        ReflectionTestUtils.setField(userController, "userService", mockUserService);
        Long userAId = 1L;
        Long userBId = 2L;
        String userAUsername = "kaybee1";
        String userBUsername = "kaybee2";


        doNothing().when(mockUserService).followUser(anyString(), eq(userBId));


        mvc.perform(
                post("/users/" + userBId + "/follows")
                    .header("Authorization", "Bearer token")
            )
            .andExpect(status().isOk());


        verify(mockUserService).followUser(anyString(), eq(userBId));
    }
    @Test
    @Transactional
    public void givenUserAFollowsUserB_whenUserAUnfollowsUserB_userBNumberOfFollowersDecreasesByOneAndUserASizeOfUsersFollowedDecreasesByOne() throws Exception {
        UserService mockUserService = mock(UserService.class);

        ReflectionTestUtils.setField(userController, "userService", mockUserService);

        Long userAId = 1L;
        Long userBId = 2L;
        String userAUsername = "kaybee1";
        String userBUsername = "kaybee2";


        doNothing().when(mockUserService).unfollowUser(anyString(), eq(userBId));


        mvc.perform(
                delete("/users/" + userBId + "/follows")
                    .header("Authorization", "Bearer token")
            )
            .andExpect(status().isOk());


        verify(mockUserService).unfollowUser(anyString(), eq(userBId));
    }
    @Test
    public void givenUserAFollowsUserB_whenUserBFollowers_jsonArrayIsReturned() throws Exception {
        UserService mockUserService = mock(UserService.class);

        User userA = new User();
        userA.setId(1L);
        userA.setUsername("kaybee1");

        List<UserDTO> followers = List.of(new UserDTO(userA));

        when(mockUserService.getFollowers("Bearer token")).thenReturn(followers);


        when(jwtService.extractUsername("Bearer token")).thenReturn("kaybee2");


        ReflectionTestUtils.setField(userController, "userService", mockUserService);


        mvc.perform(
                get("/users/followers")
                    .header("Authorization", "Bearer token")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.*", hasSize(1)))
            .andExpect(jsonPath("$[0].username", is("kaybee1")));
    }

    @Test
    public void givenUserAFollowsUserB_whenUserAFollowing_jsonArrayIsReturned() throws Exception {
        UserService mockUserService = mock(UserService.class);

        User userB = new User();
        userB.setId(2L);
        userB.setUsername("kaybee2");

        List<UserDTO> following = List.of(new UserDTO(userB));

        when(mockUserService.getUsersFollowed("Bearer token")).thenReturn(following);


        when(jwtService.extractUsername("Bearer token")).thenReturn("kaybee1");


        ReflectionTestUtils.setField(userController, "userService", mockUserService);


        mvc.perform(
                get("/users/following")
                    .header("Authorization", "Bearer token")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.*", hasSize(1)))
            .andExpect(jsonPath("$[0].username", is("kaybee2")));
    }


    @Test
    public void givenUserAndArticle_whenUserBookmarksArticle_thenArticleIsBookmarked() throws Exception {
        var article = new Article("test title 1", "test content 1", userRepository.findById(1L).get(), null);

        articleRepository.save(article);
        Long articleId = 1L;

        mvc.perform(
                post("/users/bookmarks/" + articleId)
                    .header("Authorization", "Bearer token")
            )
            .andExpect(status().isOk());


        User userAfterBookmarking = userRepository.findByIdWithBookmarkedArticles(1L).get();
        assertTrue(userAfterBookmarking.getBookmarkedArticles().stream()
            .anyMatch(foundArticle -> foundArticle.getId().equals(articleId)));
    }

    @Test
    public void givenUserAndBookmarkedArticle_whenUserUnBookmarksArticle_thenArticleIsUnBookmarked() throws Exception {
        var article = new Article("test title 1", "test content 1", userRepository.findById(1L).get(), null);

        articleRepository.save(article);
        Long articleId = 1L;


        userService.bookmarkArticle(articleId, "Bearer token");

        mvc.perform(
                delete("/users/bookmarks/" + articleId)
                    .header("Authorization", "Bearer token")
            )
            .andExpect(status().isOk());


        User userAfterUnBookmarking = userRepository.findByIdWithBookmarkedArticles(1L).get();
        assertFalse(userAfterUnBookmarking.getBookmarkedArticles().stream()
            .anyMatch(foundArticle -> foundArticle.getId().equals(articleId)));
    }

    @Test
    public void givenUser_whenGetBookmarkedArticles_thenReturnJsonArrayOfBookmarkedArticles() throws Exception {
        var article = new Article("test title 1", "test content 1", userRepository.findById(1L).get(), null);

        articleRepository.save(article);
        Long articleId = 1L;


        userService.bookmarkArticle(articleId, "Bearer token");

        mvc.perform(
                get("/users/bookmarks")
                    .header("Authorization", "Bearer token")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.*", hasSize(greaterThan(0))))
            .andExpect(jsonPath("$[0].id", is(notNullValue())))
            .andExpect(jsonPath("$[0].title", is(notNullValue())));
    }

    @Test
    public void givenPaymentDetails_whenPaymentSucceeds_thenUserBecomesPremium() throws Exception {

        PaymentDetails paymentDetails = new PaymentDetails();
        User user = userRepository.findById(1L).get();
        assertFalse(user.isPremium());

        when(geoService.getLocationInfo(anyString())).thenReturn("Nigeria");

        when(paymentService.processPayment(ArgumentMatchers.any())).thenReturn(true);


        mvc.perform(
                post("/users/premium")
                    .header("Authorization", "Bearer token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(paymentDetails))
            )
            .andExpect(status().isOk())
            .andExpect(content().string("Payment successful and premium status updated."));


        User updatedUser = userRepository.findById(1L).get();
        assertTrue(updatedUser.isPremium());
    }

    @Test
    public void givenPaymentDetails_whenPaymentFails_thenUserRemainsNonPremium() throws Exception {

        PaymentDetails paymentDetails = new PaymentDetails();
        User user = userRepository.findById(1L).get();
        assertFalse(user.isPremium());

        when(geoService.getLocationInfo(anyString())).thenReturn("Nigeria");

        when(paymentService.processPayment(ArgumentMatchers.any())).thenReturn(false);


        mvc.perform(
                post("/users/premium")
                    .header("Authorization", "Bearer token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(paymentDetails))
            )
            .andExpect(status().isOk())
            .andExpect(content().string("Payment was not successful. Please try again."));


        User updatedUser = userRepository.findById(1L).get();
        assertFalse(updatedUser.isPremium());
    }

    @Test
    public void givenPaymentDetailsFromBannedCountry_whenAttemptToBecomePremium_thenThrowGeoException() throws Exception {
        PaymentDetails paymentDetails = new PaymentDetails();
        when(geoService.getLocationInfo(anyString())).thenReturn("Canada");
        when(geoService.isCountryBanned(anyString())).thenReturn(true);
        when(paymentService.processPayment(ArgumentMatchers.any())).thenReturn(true);

        mvc.perform(
                post("/users/premium")
                    .header("Authorization", "Bearer token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(paymentDetails))
            )
            .andExpect(status().isForbidden())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof GeoException))
            .andExpect(content().string(containsString("Access from your country is not allowed")));

        User user = userRepository.findById(1L).get();
        assertFalse(user.isPremium());
    }


}
