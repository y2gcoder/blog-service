package com.y2gcoder.blog.repository.comment;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.y2gcoder.blog.entity.comment.Comment;
import com.y2gcoder.blog.repository.comment.dto.CommentDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.y2gcoder.blog.entity.article.QArticle.article;
import static com.y2gcoder.blog.entity.comment.QComment.comment;
import static com.y2gcoder.blog.entity.member.QMember.member;

@Repository
public class CommentQueryRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public CommentQueryRepository(JPAQueryFactory jpaQueryFactory) {
		this.jpaQueryFactory = jpaQueryFactory;
	}

	@Transactional(readOnly = true)
	public Slice<CommentDto> findAllWithCommenter(CommentCondition condition) {
		PageRequest pageRequest = PageRequest.of(0, condition.getSize());
		Long articleId = condition.getArticleId();
		Long lastCommentId = condition.getLastCommentId();

		List<Comment> result = jpaQueryFactory.selectFrom(comment)
				.join(comment.article, article)
				.fetchJoin()
				.join(comment.member, member)
				.fetchJoin()
				.where(greaterThantLastId(lastCommentId), articleIdEq(articleId))
				.orderBy(comment.id.asc())
				.limit(pageRequest.getPageSize() + 1)
				.fetch();

		List<CommentDto> content = result.stream()
				.map(CommentDto::new)
				.collect(Collectors.toList());
		boolean hasNext = false;
		if (content.size() > pageRequest.getPageSize()) {
			content.remove(pageRequest.getPageSize());
			hasNext = true;
		}

		return new SliceImpl<>(content, pageRequest, hasNext);
	}

	private BooleanExpression greaterThantLastId(Long lastCommentId) {
		return lastCommentId != null ? comment.id.gt(lastCommentId) : null;
	}

	private BooleanExpression articleIdEq(Long articleId) {
		return articleId != null ? comment.article.id.eq(articleId) : null;
	}
}
