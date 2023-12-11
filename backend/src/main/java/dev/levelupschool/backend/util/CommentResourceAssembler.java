package dev.levelupschool.backend.util;

import dev.levelupschool.backend.controller.CommentController;
import dev.levelupschool.backend.data.model.Comment;
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
public class CommentResourceAssembler implements RepresentationModelAssembler<Comment, EntityModel<Comment>> {
    @Autowired
    private PagedResourcesAssembler<Comment> pagedResourcesAssembler;

    @Override
    public EntityModel<Comment> toModel(Comment comment) {
        EntityModel<Comment> model = EntityModel.of(comment,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CommentController.class).show(comment.getId())).withSelfRel()
        );
        return model;
    }

    public PagedModel<EntityModel<Comment>> toPagedModel(Page<Comment> comments, Pageable pageable) {
        return pagedResourcesAssembler.toModel(comments, this);
    }
}
