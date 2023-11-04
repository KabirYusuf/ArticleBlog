package dev.levelupschool.backend.controller;
import dev.levelupschool.backend.data.dto.request.CreateAuthorRequest;
import dev.levelupschool.backend.data.dto.request.UpdateAuthorRequest;
import dev.levelupschool.backend.data.dto.response.CreateAuthorResponse;
import dev.levelupschool.backend.data.model.Author;
import dev.levelupschool.backend.service.interfaces.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService authorService;
    public AuthorController(AuthorService authorService){
        this.authorService = authorService;
    }

    @PostMapping
    public ResponseEntity<CreateAuthorResponse> createAuthor(@RequestBody CreateAuthorRequest createAuthorRequest){
        return new ResponseEntity<>(authorService.createAuthor(createAuthorRequest), HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors(){
        return ResponseEntity.ok(authorService.findAllAuthors());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable("id") Long id){
        return ResponseEntity.ok(authorService.findAuthorById(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(@RequestBody UpdateAuthorRequest updateAuthorRequest, @PathVariable("id") Long id){
        return ResponseEntity.ok(authorService.updateAuthor(updateAuthorRequest, id));
    }
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long id){
        authorService.deleteAuthor(id);
    }
}
