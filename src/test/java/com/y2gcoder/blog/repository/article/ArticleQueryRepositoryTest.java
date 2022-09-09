package com.y2gcoder.blog.repository.article;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.y2gcoder.blog.entity.article.Article;
import com.y2gcoder.blog.entity.category.Category;
import com.y2gcoder.blog.entity.member.Member;
import com.y2gcoder.blog.entity.member.MemberRole;
import com.y2gcoder.blog.repository.article.dto.ArticleListDto;
import com.y2gcoder.blog.repository.category.CategoryRepository;
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
@Import({ArticleQueryRepositoryTest.TestConfig.class, ArticleQueryRepository.class})
@DataJpaTest
class ArticleQueryRepositoryTest {
	@Autowired
	ArticleRepository articleRepository;
	@Autowired
	ArticleQueryRepository articleQueryRepository;
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	CategoryRepository categoryRepository;


	Member admin;
	Category category1;
	Category category2;

	@BeforeEach
	void beforeEach() {
		admin = memberRepository.save(new Member("admin@admin.com", "!q2w3e4r", MemberRole.ROLE_ADMIN));
		category1 = categoryRepository.save(new Category("category1"));
		category2 = categoryRepository.save(new Category("category2"));
		articleRepository.saveAll(
				IntStream.range(0, 100)
						.mapToObj(
								i -> new Article(
										i == 93 ? "검색 제목" : "title "+i,
										i == 97 ? "검색 내용" : "content "+i,
										"thumbnail "+i,
										i % 2 == 0 ? category1 : category2,
										admin
								)
						)
						.collect(Collectors.toList())
		);
	}

	@Test
	@DisplayName("게시글: 목록 조회, 성공, 조건 없음.")
	void findAllByCondition_Normal_Success() {
		//given
		ArticleSearchCondition condition = new ArticleSearchCondition(
				10,
				null,
				null,
				null
		);
		//when
		Slice<ArticleListDto> result = articleQueryRepository.findAllByCondition(condition);
		//then
		assertThat(result.getNumberOfElements()).isEqualTo(10);
	}
	
	@Test
	@DisplayName("게시글: 목록 조회, 성공, 마지막 게시글 ID")
	void findAllByCondition_LastArticleId_Success() {
		//given
		ArticleSearchCondition condition = new ArticleSearchCondition(
				10,
				null,
				11L,
				null
		);
		//when
		Slice<ArticleListDto> result = articleQueryRepository.findAllByCondition(condition);
		//then
		assertThat(result.getNumberOfElements()).isEqualTo(10);
		assertThat(result.hasNext()).isFalse();
		assertThat(result.getContent().get(0).getTitle()).isEqualTo("title 9");
	}
	
	@Test
	@DisplayName("게시글: 목록 조회, 성공, 카테고리 ID")
	void findAllByCondition_CategoryId_Success() {
		//given
		ArticleSearchCondition condition = new ArticleSearchCondition(
				10,
				category1.getId(),
				null,
				null
		);
		//when
		//
		Slice<ArticleListDto> result = articleQueryRepository.findAllByCondition(condition);
		//then
		// 99, 97, 95, 93, 91, 89, 87, 85, 83, 81
		List<Long> ids = result.getContent().stream().map(ArticleListDto::getId).collect(Collectors.toList());
		assertThat(ids.stream().anyMatch(id -> id % 2 == 0)).isFalse();
	}

	@Test
	@DisplayName("게시글: 목록 조회, 성공, 검색텍스트")
	void findAllByCondition_SearchText_Success() {
		//given
		ArticleSearchCondition condition = new ArticleSearchCondition(
				10,
				null,
				null,
				"검색"
		);
		//when
		Slice<ArticleListDto> result = articleQueryRepository.findAllByCondition(condition);
		//then
		assertThat(result.getNumberOfElements()).isEqualTo(2);
		assertThat(result.getContent().get(0).getContent()).isEqualTo("검색 내용");
		assertThat(result.getContent().get(1).getTitle()).isEqualTo("검색 제목");
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