package com.y2gcoder.blog.service.article.dto;

import com.y2gcoder.blog.repository.article.dto.ArticleListDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleListResponse {
	private boolean hasNext;
	private List<ArticleListDto> content;

	public ArticleListResponse(Slice<ArticleListDto> slice) {
		this.hasNext = slice.hasNext();
		this.content = slice.getContent();
	}
}
