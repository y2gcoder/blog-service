package com.y2gcoder.blog.service.article.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.y2gcoder.blog.entity.article.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDetailResponse {
	private Long id;
	private String title;
	private String content;
	private String thumbnailUrl;
	@JsonFormat(
			shape = JsonFormat.Shape.STRING,
			pattern = "yyyy-MM-dd'T'HH:mm:ss",
			timezone = "Asia/Seoul"
	)
	private LocalDateTime createdAt;

	public ArticleDetailResponse(Article article) {
		this.id = article.getId();
		this.title = article.getTitle();
		this.content = article.getContent();
		this.thumbnailUrl = article.getThumbnailUrl();
		this.createdAt = article.getCreatedAt();
	}
}
