package com.y2gcoder.blog.service.article;

import com.y2gcoder.blog.entity.article.Article;
import com.y2gcoder.blog.entity.category.Category;
import com.y2gcoder.blog.entity.member.Member;
import com.y2gcoder.blog.entity.member.MemberRole;
import com.y2gcoder.blog.repository.article.ArticleQueryRepository;
import com.y2gcoder.blog.repository.article.ArticleRepository;
import com.y2gcoder.blog.repository.article.ArticleSearchCondition;
import com.y2gcoder.blog.repository.category.CategoryRepository;
import com.y2gcoder.blog.repository.member.MemberRepository;
import com.y2gcoder.blog.service.article.dto.ArticleCreateRequest;
import com.y2gcoder.blog.service.article.dto.ArticleDetailResponse;
import com.y2gcoder.blog.service.article.dto.ArticleListResponse;
import com.y2gcoder.blog.service.article.dto.ArticleUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

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

	@Mock
	ArticleQueryRepository articleQueryRepository;

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

	@Test
	@DisplayName("게시글: 삭제 성공")
	void delete_Normal_Success() {
		//given
		Article article = new Article("title", "content", "", createCategory(), createMember());
		given(articleRepository.findById(anyLong())).willReturn(Optional.of(article));
		//when
	  articleService.delete(1L);
		//then
		verify(articleRepository).delete(any());
	}

	@Test
	@DisplayName("게시글: 삭제 실패, 게시글 없음")
	void delete_NotFoundArticle_Fail() {
		//given
		given(articleRepository.findById(anyLong())).willReturn(Optional.empty());
		//when
		//then
		assertThatThrownBy(() -> articleService.delete(1L))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("게시글: 수정, 성공")
	void update_Normal_Success() {
		//given
		Article article = new Article(
				"title",
				"content",
				"1",
				createCategory(),
				createMember()
		);
		given(articleRepository.findById(anyLong())).willReturn(Optional.of(article));
		ArticleUpdateRequest req = new ArticleUpdateRequest("제목", "내용", "2");
		//when
		articleService.update(1L, req);
		//then
		assertThat(article.getTitle()).isEqualTo(req.getTitle());
		assertThat(article.getContent()).isEqualTo(req.getContent());
		assertThat(article.getThumbnailUrl()).isEqualTo(req.getThumbnailUrl());
	}

	@Test
	@DisplayName("게시글: 수정, 실패, 게시글 없음")
	void update_NotFoundArticle_Fail() {
		//given
		given(articleRepository.findById(anyLong())).willReturn(Optional.empty());
		//when
		//then
		assertThatThrownBy(() -> articleService.update(
				1L,
				new ArticleUpdateRequest("제목", "내용", "2")
		))
				.isInstanceOf(IllegalArgumentException.class);
	}
	
	@Test
	@DisplayName("게시글: 목록 조회, 성공")
	void readAll_Normal_Success() {
		//given
		given(articleQueryRepository.findAllByCondition(any())).willReturn(Page.empty());
		//when
		ArticleSearchCondition condition = new ArticleSearchCondition(
				1,
				null,
				null,
				null
		);
		ArticleListResponse result = articleService.readAll(condition);
		//then
		assertThat(result.getContent().size()).isZero();
		verify(articleQueryRepository).findAllByCondition(condition);
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