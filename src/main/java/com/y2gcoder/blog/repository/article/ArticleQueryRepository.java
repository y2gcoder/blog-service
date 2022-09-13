package com.y2gcoder.blog.repository.article;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.y2gcoder.blog.entity.article.Article;
import com.y2gcoder.blog.repository.article.dto.ArticleListDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.y2gcoder.blog.entity.article.QArticle.article;
import static com.y2gcoder.blog.entity.category.QCategory.category;

@Repository
public class ArticleQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	public ArticleQueryRepository(JPAQueryFactory jpaQueryFactory) {
		this.jpaQueryFactory = jpaQueryFactory;
	}

	@Transactional(readOnly = true)
	public Slice<ArticleListDto> findAllByCondition(ArticleSearchCondition condition) {
		PageRequest pageRequest = PageRequest.of(0, condition.getSize());
		Long categoryId = condition.getCategoryId();
		Long lastArticleId = condition.getLastArticleId();
		String searchText = condition.getSearchText();

		List<Article> result = jpaQueryFactory.selectFrom(article)
				.join(article.category, category)
				.fetchJoin()
				.where(findSearchText(searchText), lowerThanLastId(lastArticleId), categoryIdEq(categoryId))
				.orderBy(article.id.desc())
				.limit(pageRequest.getPageSize() + 1)
				.fetch();

		List<ArticleListDto> content = result.stream()
				.map(ArticleListDto::new)
				.collect(Collectors.toList());
		boolean hasNext = false;
		if (content.size() > pageRequest.getPageSize()) {
			content.remove(pageRequest.getPageSize());
			hasNext = true;
		}

		return new SliceImpl<>(content, pageRequest, hasNext);
	}

	private BooleanExpression findSearchText(String searchText) {
		return StringUtils.hasText(searchText) ? findSearchTextFromTitle(searchText).or(findSearchTextFromContent(searchText)) : null;
	}

	private BooleanExpression findSearchTextFromTitle(String searchText) {
		return article.title.containsIgnoreCase(searchText);
	}

	private BooleanExpression findSearchTextFromContent(String searchText) {
		return article.content.containsIgnoreCase(searchText);
	}

	private BooleanExpression lowerThanLastId(Long lastArticleId) {
		return lastArticleId != null ? article.id.lt(lastArticleId) : null;
	}


	private BooleanExpression categoryIdEq(Long categoryId) {
		return categoryId != null ? article.category.id.eq(categoryId) : null;
	}


}
