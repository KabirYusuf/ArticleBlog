package dev.levelupschool.backend.util;

import dev.levelupschool.backend.controller.ArticleController;
import dev.levelupschool.backend.data.model.Article;
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
public class ArticleResourceAssembler implements RepresentationModelAssembler<Article, EntityModel<Article>> {
    @Autowired
    private PagedResourcesAssembler<Article> pagedResourcesAssembler;

    @Override
    public EntityModel<Article> toModel(Article article) {
        EntityModel<Article> model = EntityModel.of(article,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ArticleController.class).show(article.getId())).withSelfRel()
        );
        return model;
    }

    public PagedModel<EntityModel<Article>> toPagedModel(Page<Article> articles, Pageable pageable) {
        return pagedResourcesAssembler.toModel(articles, this);
    }
}
