//package dev.levelupschool.backend.controller;
//
//import dev.levelupschool.backend.data.dto.request.CreateUserRequest;
//import dev.levelupschool.backend.data.dto.request.UpdateUserRequest;
//import dev.levelupschool.backend.data.repository.UserRepository;
//import dev.levelupschool.backend.service.interfaces.UserService;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.web.servlet.MockMvc;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//import static dev.levelupschool.backend.util.Serializer.*;
//import static org.hamcrest.Matchers.hasSize;
//import static org.hamcrest.Matchers.is;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@AutoConfigureMockMvc
//@SpringBootTest
//@Testcontainers
//@ActiveProfiles("test-container")
//@Sql(scripts = "classpath:reset-database.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//class UserControllerTest {
//    @Autowired
//    private MockMvc mvc;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private UserService userService;
//    private CreateUserRequest createUserRequest;
//
//    @Container
//    @ServiceConnection
//    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:latest")
//        .withDatabaseName("levelup");
//    @BeforeAll
//    static  void beforeAll(){
//        postgreSQLContainer.start();
//    }
//    @AfterAll
//    static void afterAll(){
//        postgreSQLContainer.stop();
//    }
//    @Test
//    void contextLoads() {
//    }
//
//    @BeforeEach
//    void setUp(){
//        userRepository.deleteAll();
//        createUserRequest = new CreateUserRequest();
//        createUserRequest.setFirstName("kabir");
//        createUserRequest.setLastName("yusuf");
//        userService.registerUser(createUserRequest);
//    }
//
//
//    @Test
//    public void givenAuthors_whenGetAuthors_thenReturnJsonArray() throws Exception {
//        mvc.perform(
//                get("/users")
//                    .contentType(MediaType.APPLICATION_JSON))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$", hasSize(1)))
//            .andExpect(jsonPath("$[0].firstName", is("Kabir")))
//            .andExpect(jsonPath("$[0].lastName", is("Yusuf")));
//
//    }
//
//    @Test
//    public void givenAuthor_whenGetAuthorWithId_thenReturnJsonOfThatSpecificAuthor() throws Exception {
//        mvc.perform(
//                get("/users/1")
//            )
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.firstName").value("Kabir"))
//            .andExpect(jsonPath("$.lastName").value("Yusuf"));
//    }
//
//    @Test
//    public void givenAuthor_whenDeleteAuthorWithId_then200IsReturnedAsStatusCode() throws Exception {
//        Assertions.assertEquals(1, userService.findAllUsers().size());
//       mvc.perform(delete("/users/1"))
//            .andExpect(status().is2xxSuccessful());
//        Assertions.assertEquals(0, userService.findAllUsers().size());
//    }
//    @Test
//    public void givenACreateAuthorRequest_whenAuthorIsCreated_theNumberOfAuthorsIncreasesByOne() throws Exception {
//        Assertions.assertEquals(1, userService.findAllUsers().size());
//
//        CreateUserRequest createUserRequest1 = new CreateUserRequest();
//        createUserRequest1.setFirstName("Luka");
//        createUserRequest1.setLastName("domagoj");
//        mvc.perform(
//            post("/users")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(createUserRequest1))
//        )
//            .andExpect(jsonPath("$.name").value("Luka Domagoj"))
//            .andExpect(status().isCreated());
//
//        Assertions.assertEquals(2, userService.findAllUsers().size());
//    }
//    @Test
//    void givenUpdateAuthorRequest_whenAuthorIsUpdated_authorIsUpdatedInDatabase() throws Exception {
//        Assertions.assertEquals("Kabir", userService.findUserById(1L).getFirstName());
//
//        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
//        updateUserRequest.setFirstName("Marko");
//        updateUserRequest.setLastName("James");
//
//       mvc.perform(
//           put("/users/1")
//               .contentType(MediaType.APPLICATION_JSON)
//               .content(asJsonString(updateUserRequest))
//       )
//           .andExpect(status().isOk());
//       Assertions.assertEquals("Marko", userService.findUserById(1L).getFirstName());
//    }
//
//}
