package com.y2gcoder.blog.service.article;

import com.y2gcoder.blog.entity.article.Article;
import com.y2gcoder.blog.entity.category.Category;
import com.y2gcoder.blog.entity.member.Member;
import com.y2gcoder.blog.repository.article.ArticleQueryRepository;
import com.y2gcoder.blog.repository.article.ArticleRepository;
import com.y2gcoder.blog.repository.article.ArticleSearchCondition;
import com.y2gcoder.blog.repository.category.CategoryRepository;
import com.y2gcoder.blog.repository.member.MemberRepository;
import com.y2gcoder.blog.service.article.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ArticleService {
	private final ArticleRepository articleRepository;
	private final MemberRepository memberRepository;
	private final CategoryRepository categoryRepository;
	private final ArticleQueryRepository articleQueryRepository;

	@Transactional
	public ArticleCreateResponse create(ArticleCreateRequest req) {
		Member member = memberRepository
				.findById(req.getMemberId())
				.orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다. id=" + req.getMemberId()));
		Category category = categoryRepository
				.findById(req.getCategoryId())
				.orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다. id=" + req.getCategoryId()));

		Article article = articleRepository
				.save(new Article(req.getTitle(), req.getContent(), req.getThumbnailUrl(), category, member));
		return new ArticleCreateResponse(article.getId());
	}

	public ArticleDetailResponse read(Long id) {
		return new ArticleDetailResponse(
				articleRepository.findById(id)
						.orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + id))
		);
	}

	@Transactional
	public void delete(Long id) {
		Article article = articleRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + id));
		articleRepository.delete(article);
	}

	@Transactional
	public ArticleUpdateResponse update(Long id, ArticleUpdateRequest req) {
		Article article = articleRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + id));
		article.changeTitle(req.getTitle());
		article.changeContent(req.getContent());
		article.changeThumbnailUrl(req.getThumbnailUrl());
		return new ArticleUpdateResponse(id);
	}

	public ArticleListResponse readAll(ArticleSearchCondition condition) {
		return new ArticleListResponse(articleQueryRepository.findAllByCondition(condition));
	}
}
