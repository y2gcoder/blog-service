package com.y2gcoder.blog.service.comment;

import com.y2gcoder.blog.entity.article.Article;
import com.y2gcoder.blog.entity.category.Category;
import com.y2gcoder.blog.entity.comment.Comment;
import com.y2gcoder.blog.entity.member.Member;
import com.y2gcoder.blog.entity.member.MemberRole;
import com.y2gcoder.blog.repository.article.ArticleRepository;
import com.y2gcoder.blog.repository.comment.CommentCondition;
import com.y2gcoder.blog.repository.comment.CommentQueryRepository;
import com.y2gcoder.blog.repository.comment.CommentRepository;
import com.y2gcoder.blog.repository.member.MemberRepository;
import com.y2gcoder.blog.service.comment.dto.CommentCreateRequest;
import com.y2gcoder.blog.service.comment.dto.CommentListResponse;
import com.y2gcoder.blog.service.comment.dto.CommentUpdateRequest;
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
class CommentServiceTest {
	@InjectMocks CommentService commentService;

	@Mock
	CommentRepository commentRepository;

	@Mock
	ArticleRepository articleRepository;

	@Mock
	MemberRepository memberRepository;

	@Mock
	CommentQueryRepository commentQueryRepository;

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

	@Test
	@DisplayName("댓글: 삭제, 성공")
	void delete_Normal_Success() {
		//given
		Comment comment = new Comment("댓글", createMember(), createArticle());
		given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));
		//when
		commentService.delete(1L);
		//then
		verify(commentRepository).delete(any());
	}

	@Test
	@DisplayName("댓글: 삭제, 실패, 댓글 없음")
	void delete_NotFoundComment_Fail() {
		//given
		given(commentRepository.findById(anyLong()))
				.willReturn(Optional.empty());
		//when
		//then
		assertThatThrownBy(() -> commentService.delete(1L))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("댓글: 수정, 성공")
	void update_Normal_Success() {
		//given
		Comment comment = new Comment(
				"comment",
				createMember(),
				createArticle()
		);
		given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));
		CommentUpdateRequest req = new CommentUpdateRequest("수정댓글");
		//when
		commentService.update(1L, req);
		//then
		assertThat(comment.getContent()).isEqualTo(req.getContent());
	}

	@Test
	@DisplayName("댓글: 수정, 실패, 댓글 없음")
	void update_NotFoundComment_Fail() {
		//given
		given(commentRepository.findById(anyLong())).willReturn(Optional.empty());
		//when
		//then
		assertThatThrownBy(() -> commentService.update(
				1L,
				new CommentUpdateRequest("수정댓글")
		)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("댓글: 목록 조회, 성공")
	void readAll_Normal_Success() {
		//given
		given(commentQueryRepository.findAllWithCommenter(any())).willReturn(Page.empty());
		//when
		CommentCondition condition = new CommentCondition();
		CommentListResponse result = commentService.readAll(condition);
		//then
		assertThat(result.getContent().size()).isZero();
		verify(commentQueryRepository).findAllWithCommenter(condition);
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