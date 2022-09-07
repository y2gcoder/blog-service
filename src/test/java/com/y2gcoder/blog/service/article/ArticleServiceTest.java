package com.y2gcoder.blog.service.article;

import com.y2gcoder.blog.entity.article.Article;
import com.y2gcoder.blog.entity.category.Category;
import com.y2gcoder.blog.entity.member.Member;
import com.y2gcoder.blog.entity.member.MemberRole;
import com.y2gcoder.blog.repository.article.ArticleRepository;
import com.y2gcoder.blog.repository.category.CategoryRepository;
import com.y2gcoder.blog.repository.member.MemberRepository;
import com.y2gcoder.blog.service.article.dto.ArticleCreateRequest;
import com.y2gcoder.blog.service.article.dto.ArticleDetailResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {
	@InjectMocks ArticleService articleService;
	@Mock
	ArticleRepository articleRepository;
	@Mock
	MemberRepository memberRepository;
	@Mock
	CategoryRepository categoryRepository;

	@Test
	@DisplayName("게시글: 생성 성공")
	void create_Normal_Success() {
		//given
		ArticleCreateRequest req = createArticleCreateRequest();
		Member member = createMember();
		given(memberRepository.findById(anyLong()))
				.willReturn(Optional.of(member));
		Category category = createCategory();
		given(categoryRepository.findById(anyLong())).willReturn(Optional.of(category));
		given(articleRepository.save(any()))
				.willReturn(
						new Article("my title", "my content", "", category, member)
				);
		//when
		articleService.create(req);
		//then
		verify(articleRepository).save(any());
	}

	@Test
	@DisplayName("게시글: 생성 실패, 카테고리 없음")
	void create_NotFoundCategory_Fail() {
		//given
		given(memberRepository.findById(anyLong()))
				.willReturn(Optional.of(createMember()));
		given(categoryRepository.findById(anyLong())).willReturn(Optional.empty());
		//when
		//then
		assertThatThrownBy(() -> articleService.create(createArticleCreateRequest()))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("게시글: 생성 실패, 회원 없음")
	void create_NotFoundMember_Fail() {
		//given
		given(memberRepository.findById(anyLong())).willReturn(Optional.empty());
		//when
		//then
		assertThatThrownBy(() -> articleService.create(createArticleCreateRequest()))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("게시글: 단건 조회 성공")
	void read_Normal_Success() {
		//given
		Article article = new Article("title", "content", "", createCategory(), createMember());
		given(articleRepository.findById(anyLong())).willReturn(Optional.of(article));
		//when
		ArticleDetailResponse result = articleService.read(1L);
		//then
		assertThat(result.getTitle()).isEqualTo(article.getTitle());
	}

	@Test
	@DisplayName("게시글: 단건 조회 실패, 게시글 없음")
	void read_NotFoundArticle_Fail() {
		//given
		given(articleRepository.findById(anyLong())).willReturn(Optional.empty());
		//when
		//then
		assertThatThrownBy(() -> articleService.read(1L))
				.isInstanceOf(IllegalArgumentException.class);
	}

	private static ArticleCreateRequest createArticleCreateRequest() {
		return new ArticleCreateRequest(
				"my title",
				"my content",
				"",
				1L,
				1L
		);
	}

	private static Category createCategory() {
		return new Category("category");
	}

	private static Member createMember() {
		return new Member("email@email.com", "!q2w3e4r", MemberRole.ROLE_USER);
	}
}