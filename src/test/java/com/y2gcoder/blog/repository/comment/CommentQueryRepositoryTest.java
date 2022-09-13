package com.y2gcoder.blog.repository.comment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.y2gcoder.blog.entity.article.Article;
import com.y2gcoder.blog.entity.category.Category;
import com.y2gcoder.blog.entity.comment.Comment;
import com.y2gcoder.blog.entity.member.Member;
import com.y2gcoder.blog.entity.member.MemberRole;
import com.y2gcoder.blog.repository.article.ArticleRepository;
import com.y2gcoder.blog.repository.category.CategoryRepository;
import com.y2gcoder.blog.repository.comment.dto.CommentDto;
import com.y2gcoder.blog.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Import({CommentQueryRepositoryTest.TestConfig.class, CommentQueryRepository.class})
@DataJpaTest
class CommentQueryRepositoryTest {

	@Autowired CommentRepository commentRepository;
	@Autowired CommentQueryRepository commentQueryRepository;
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	ArticleRepository articleRepository;

	Member author;
	Member commenter;
	Category category;
	Article article1;
	Article article2;

	@BeforeEach
	void beforeEach() {
		author = memberRepository.save(new Member("author@author.com", "!q2w3e4r", MemberRole.ROLE_ADMIN));
		commenter = memberRepository
				.save(new Member("commenter@commenter.com", "!q2w3e4r", MemberRole.ROLE_USER));
		category = categoryRepository.save(new Category("category"));
		article1 = articleRepository
				.save(new Article("게시글", "게시글내용", "1", category, author));
		article1 = articleRepository
				.save(new Article("Article", "Article Content", "2", category, author));

		commentRepository.saveAll(
				IntStream.range(0, 100)
						.mapToObj(
								i -> new Comment(
										"comment "+i,
										i % 5 == 0 ? author : commenter,
										i % 2 == 0 ? article2 : article1
								)
						).collect(Collectors.toList())
		);
	}

	@Test
	@DisplayName("댓글: 목록 조회, 성공, 조건 없음")
	void findAllWithCommenter_Normal_Success() {
		//given
		CommentCondition condition = new CommentCondition(
				10,
				null,
				null
		);
		//when
		Slice<CommentDto> result = commentQueryRepository.findAllWithCommenter(condition);
		//then
		assertThat(result.getNumberOfElements()).isEqualTo(10);
	}

	@Test
	@DisplayName("댓글: 목록 조회, 성공, 마지막 댓글 ID")
	void findAllWithCommenter_LastCommentId_Success() {
		//given
		List<Comment> comments = commentRepository.findAll();
		CommentCondition condition = new CommentCondition(
				10,
				null,
				comments.get(8).getId()
		);
		//when
		Slice<CommentDto> result = commentQueryRepository.findAllWithCommenter(condition);
		//then
		assertThat(result.hasNext()).isTrue();
		assertThat(result.getContent().get(0).getContent()).isEqualTo("comment 9");
	}

	@Test
	@DisplayName("댓글: 목록 조회, 성공, 게시글 ID")
	void findAllWithCommenter_ArticleId_Success() {
		//given
		CommentCondition condition = new CommentCondition(
				10,
				article1.getId(),
				null
		);
		//when
		Slice<CommentDto> result = commentQueryRepository.findAllWithCommenter(condition);
		//then
		// 2 4 6 8 10 12 14 16 18 20
		List<Long> ids = result.getContent().stream().map(CommentDto::getId).collect(Collectors.toList());
		assertThat(ids.stream().allMatch(id -> id % 2 == 0)).isTrue();
	}

	@TestConfiguration
	public static class TestConfig {
		@PersistenceContext
		private EntityManager entityManager;

		@Bean
		public JPAQueryFactory jpaQueryFactory() {
			return new JPAQueryFactory(entityManager);
		}
	}
}