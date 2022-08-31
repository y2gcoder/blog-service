package com.y2gcoder.blog.repository.article;

import com.y2gcoder.blog.entity.article.Article;
import com.y2gcoder.blog.entity.category.Category;
import com.y2gcoder.blog.entity.member.Member;
import com.y2gcoder.blog.entity.member.MemberRole;
import com.y2gcoder.blog.repository.category.CategoryRepository;
import com.y2gcoder.blog.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ArticleRepositoryTest {
	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	ArticleRepository articleRepository;

	@PersistenceContext
	EntityManager em;

	Category category;
	Member member;

	@BeforeEach
	void beforeEach() {
		category = categoryRepository.save(createCategory());
		member = memberRepository.save(createMember());
	}

	@Test
	@DisplayName("Article: 저장 성공")
	void saveArticle_Normal_Success() {
		//given
		Article article = createArticle(category, member);
		//when
		articleRepository.save(article);
		//then
		assertThat(articleRepository.findById(article.getId()).isPresent()).isTrue();
	}

	@Test
	@DisplayName("Article: 카테고리 변경 성공")
	void changeArticle_Normal_Success() {
		//given
		Article article = articleRepository.save(createArticle(category, member));
		//when
		Category changedCategory = categoryRepository.save(createCategory("카테고리2"));
		article.changeCategory(changedCategory);
		em.flush();
		em.clear();
		//then
		Article findArticle = articleRepository.findById(article.getId()).get();
		assertThat(findArticle.getCategory().getName()).isEqualTo("카테고리2");
	}

	private static Category createCategory() {
		return new Category("카테고리");
	}

	private static Category createCategory(String categoryName) {
		return new Category(categoryName);
	}

	private static Member createMember() {
		return new Member("email@email.com", "12345", MemberRole.ROLE_USER);
	}

	private static Article createArticle(Category category, Member member) {
		return new Article("제목", "내용", "썸네일", category, member);
	}
}