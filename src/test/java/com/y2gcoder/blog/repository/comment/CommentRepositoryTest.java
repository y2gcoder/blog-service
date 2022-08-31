package com.y2gcoder.blog.repository.comment;

import com.y2gcoder.blog.entity.article.Article;
import com.y2gcoder.blog.entity.category.Category;
import com.y2gcoder.blog.entity.comment.Comment;
import com.y2gcoder.blog.entity.member.Member;
import com.y2gcoder.blog.entity.member.MemberRole;
import com.y2gcoder.blog.repository.article.ArticleRepository;
import com.y2gcoder.blog.repository.category.CategoryRepository;
import com.y2gcoder.blog.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CommentRepositoryTest {
	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	ArticleRepository articleRepository;
	@Autowired
	CommentRepository commentRepository;

	Category category;
	Member author;
	Article article;
	Member commenter;

	@BeforeEach
	void beforeEach() {
		category = categoryRepository.save(createCategory());
		author = memberRepository.save(createMember("author@author.com"));
		article = articleRepository.save(createArticle(category, author));
		commenter = memberRepository.save(createMember());
	}

	@Test
	@DisplayName("Comment: 저장 성공")
	void saveComment_Normal_Success() {
		//given
		Comment comment = new Comment("댓글", commenter, article);
		//when
		commentRepository.save(comment);
		//then
		assertThat(commentRepository.findById(comment.getId()).isPresent()).isTrue();
	}

	private static Category createCategory() {
		return new Category("카테고리");
	}

	private static Member createMember() {
		return new Member("email@email.com", "12345", MemberRole.ROLE_USER);
	}

	private static Member createMember(String email) {
		return new Member(email, "12345", MemberRole.ROLE_USER);
	}

	private static Article createArticle(Category category, Member member) {
		return new Article("제목", "내용", "썸네일", category, member);
	}
}