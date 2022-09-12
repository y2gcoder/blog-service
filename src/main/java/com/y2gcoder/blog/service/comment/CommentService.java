package com.y2gcoder.blog.service.comment;

import com.y2gcoder.blog.entity.article.Article;
import com.y2gcoder.blog.entity.comment.Comment;
import com.y2gcoder.blog.entity.member.Member;
import com.y2gcoder.blog.repository.article.ArticleRepository;
import com.y2gcoder.blog.repository.comment.CommentRepository;
import com.y2gcoder.blog.repository.member.MemberRepository;
import com.y2gcoder.blog.service.comment.dto.CommentCreateRequest;
import com.y2gcoder.blog.service.comment.dto.CommentCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {
	private final CommentRepository commentRepository;
	private final MemberRepository memberRepository;
	private final ArticleRepository articleRepository;

	@Transactional
	public CommentCreateResponse create(CommentCreateRequest req) {
		Article article = articleRepository.findById(req.getArticleId())
				.orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + req.getArticleId()));
		Member member = memberRepository.findById(req.getMemberId())
				.orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다. id=" + req.getMemberId()));
		Comment comment = commentRepository.save(
				new Comment(req.getContent(), member, article)
		);
		return new CommentCreateResponse(comment.getId());
	}
}
