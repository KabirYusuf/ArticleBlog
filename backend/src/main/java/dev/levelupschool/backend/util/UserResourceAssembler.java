package dev.levelupschool.backend.util;

import dev.levelupschool.backend.controller.UserController;
import dev.levelupschool.backend.data.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class UserResourceAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {
    @Autowired
    private PagedResourcesAssembler<User> pagedResourcesAssembler;

    @Override
    public EntityModel<User> toModel(User user) {
        EntityModel<User> model = EntityModel.of(user,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserById(user.getId())).withSelfRel()
        );
        return model;
    }

    public PagedModel<EntityModel<User>> toPagedModel(Page<User> users, Pageable pageable) {
        return pagedResourcesAssembler.toModel(users, this);
    }
}
