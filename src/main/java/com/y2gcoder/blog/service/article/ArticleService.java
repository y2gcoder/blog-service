package com.y2gcoder.blog.service.article;

import com.y2gcoder.blog.entity.article.Article;
import com.y2gcoder.blog.entity.category.Category;
import com.y2gcoder.blog.entity.member.Member;
import com.y2gcoder.blog.repository.article.ArticleRepository;
import com.y2gcoder.blog.repository.category.CategoryRepository;
import com.y2gcoder.blog.repository.member.MemberRepository;
import com.y2gcoder.blog.service.article.dto.ArticleCreateRequest;
import com.y2gcoder.blog.service.article.dto.ArticleCreateResponse;
import com.y2gcoder.blog.service.article.dto.ArticleDetailResponse;
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
}
