package com.y2gcoder.blog.service.comment;

import com.y2gcoder.blog.entity.article.Article;
import com.y2gcoder.blog.entity.category.Category;
import com.y2gcoder.blog.entity.comment.Comment;
import com.y2gcoder.blog.entity.member.Member;
import com.y2gcoder.blog.entity.member.MemberRole;
import com.y2gcoder.blog.repository.article.ArticleRepository;
import com.y2gcoder.blog.repository.comment.CommentRepository;
import com.y2gcoder.blog.repository.member.MemberRepository;
import com.y2gcoder.blog.service.comment.dto.CommentCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
	@InjectMocks CommentService commentService;

	@Mock
	CommentRepository commentRepository;

	@Mock
	ArticleRepository articleRepository;

	@Mock
	MemberRepository memberRepository;

	@Test
	@DisplayName("댓글: 생성, 성공")
	void create_Normal_Success() {
		//given
		Member member = createMember();
		given(memberRepository.findById(anyLong()))
				.willReturn(Optional.of(member));
		Article article = createArticle();
		given(articleRepository.findById(anyLong()))
				.willReturn(Optional.of(article));
		given(commentRepository.save(any())).willReturn(new Comment("content", member, article));
		//when
		commentService.create(createCommentCreateRequest());
		//then
		verify(commentRepository).save(any());
	}

	@Test
	@DisplayName("댓글: 생성, 실패, 게시글 없음")
	void create_NotFoundArticle_Fail() {
		//given
		given(articleRepository.findById(anyLong())).willReturn(Optional.empty());
		//when
		//then
		assertThatThrownBy(() -> commentService.create(createCommentCreateRequest()))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("댓글: 생성, 실패, 작성자 없음")
	void create_NotFoundMember_Fail() {
		//given
		given(articleRepository.findById(anyLong())).willReturn(Optional.of(createArticle()));
		given(memberRepository.findById(anyLong())).willReturn(Optional.empty());
		//when
		//then
		assertThatThrownBy(() -> commentService.create(createCommentCreateRequest()))
				.isInstanceOf(IllegalArgumentException.class);
	}

	private static CommentCreateRequest createCommentCreateRequest() {
		return new CommentCreateRequest("content", 1L, 1L);
	}

	private static Member createMember() {
		return new Member("email@email.com", "!q2w3e4r", MemberRole.ROLE_USER);
	}

	private static Article createArticle() {
		return new Article(
				"title",
				"content",
				"",
				new Category("category"),
				createMember()
		);
	}
}