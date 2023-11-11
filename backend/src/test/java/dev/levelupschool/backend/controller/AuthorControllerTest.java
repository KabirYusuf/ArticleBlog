package dev.levelupschool.backend.controller;

import dev.levelupschool.backend.data.dto.request.CreateAuthorRequest;
import dev.levelupschool.backend.data.dto.request.UpdateAuthorRequest;
import dev.levelupschool.backend.data.repository.AuthorRepository;
import dev.levelupschool.backend.service.interfaces.AuthorService;
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
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Testcontainers
@ActiveProfiles("test-container")
@Sql(scripts = "classpath:reset-database.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AuthorControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private AuthorService authorService;
    private CreateAuthorRequest createAuthorRequest;

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
        authorRepository.deleteAll();
        createAuthorRequest = new CreateAuthorRequest();
        createAuthorRequest.setFirstName("kabir");
        createAuthorRequest.setLastName("yusuf");
        authorService.createAuthor(createAuthorRequest);
    }


    @Test
    public void givenAuthors_whenGetAuthors_thenReturnJsonArray() throws Exception {
        mvc.perform(
                get("/authors")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].firstName", is("Kabir")))
            .andExpect(jsonPath("$[0].lastName", is("Yusuf")));

    }

    @Test
    public void givenAuthor_whenGetAuthorWithId_thenReturnJsonOfThatSpecificAuthor() throws Exception {
        mvc.perform(
                get("/authors/1")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value("Kabir"))
            .andExpect(jsonPath("$.lastName").value("Yusuf"));
    }

    @Test
    public void givenAuthor_whenDeleteAuthorWithId_then200IsReturnedAsStatusCode() throws Exception {
        Assertions.assertEquals(1, authorService.findAllAuthors().size());
       mvc.perform(delete("/authors/1"))
            .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals(0, authorService.findAllAuthors().size());
    }
    @Test
    public void givenACreateAuthorRequest_whenAuthorIsCreated_theNumberOfAuthorsIncreasesByOne() throws Exception {
        Assertions.assertEquals(1, authorService.findAllAuthors().size());

        CreateAuthorRequest createAuthorRequest1 = new CreateAuthorRequest();
        createAuthorRequest1.setFirstName("Luka");
        createAuthorRequest1.setLastName("domagoj");
        mvc.perform(
            post("/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(createAuthorRequest1))
        )
            .andExpect(jsonPath("$.name").value("Luka Domagoj"))
            .andExpect(status().isCreated());

        Assertions.assertEquals(2, authorService.findAllAuthors().size());
    }
    @Test
    void givenUpdateAuthorRequest_whenAuthorIsUpdated_authorIsUpdatedInDatabase() throws Exception {
        Assertions.assertEquals("Kabir", authorService.findAuthorById(1L).getFirstName());

        UpdateAuthorRequest updateAuthorRequest = new UpdateAuthorRequest();
        updateAuthorRequest.setFirstName("Marko");
        updateAuthorRequest.setLastName("James");

       mvc.perform(
           put("/authors/1")
               .contentType(MediaType.APPLICATION_JSON)
               .content(asJsonString(updateAuthorRequest))
       )
           .andExpect(status().isOk());
       Assertions.assertEquals("Marko", authorService.findAuthorById(1L).getFirstName());
    }

}
