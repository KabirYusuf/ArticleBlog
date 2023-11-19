package dev.levelupschool.backend.controller;
import dev.levelupschool.backend.data.dto.request.AuthenticationRequest;
import dev.levelupschool.backend.data.dto.request.UpdateUserRequest;
import dev.levelupschool.backend.data.model.Article;
import dev.levelupschool.backend.data.model.User;
import dev.levelupschool.backend.service.interfaces.UserService;
import dev.levelupschool.backend.util.UserResourceAssembler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserResourceAssembler userResourceAssembler;
    public UserController(
        UserService userService,
        UserResourceAssembler userResourceAssembler){
        this.userService = userService;
        this.userResourceAssembler = userResourceAssembler;
    }

    @PostMapping
    public ResponseEntity<User> createAuthor(@RequestBody AuthenticationRequest authenticationRequest){
        return new ResponseEntity<>(userService.registerUser(authenticationRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<User>>> getAllUsers(Pageable pageable) {
        Page<User> users = userService.findAllUsers(pageable);
        PagedModel<EntityModel<User>> pagedModel = userResourceAssembler.toPagedModel(users, pageable);

        return ResponseEntity.ok(pagedModel);
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id){
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @GetMapping("/me")
    public ResponseEntity<User> getUser(HttpServletRequest httpServletRequest){
        String authHeader = httpServletRequest.getHeader("Authorization");
        return ResponseEntity.ok(userService.getUser(authHeader));
    }
    @PutMapping("/{id}")
    public ResponseEntity<User> updateAuthor(@RequestBody UpdateUserRequest updateUserRequest, @PathVariable("id") Long id){
        return ResponseEntity.ok(userService.updateUser(updateUserRequest, id));
    }
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long id){
        userService.deleteUser(id);
    }
}
