package com.y2gcoder.blog.repository.article;

import com.y2gcoder.blog.entity.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
