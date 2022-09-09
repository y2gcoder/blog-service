package com.y2gcoder.blog.repository.article;

import lombok.Data;

@Data
public class ArticleSearchCondition {
	private int size;
	private Long categoryId;
	private Long lastArticleId;
	private String searchText;

	public ArticleSearchCondition() {
		this.size = 10;
	}

	public ArticleSearchCondition(int size, Long categoryId, Long lastArticleId, String searchText) {
		if (size <= 0) {
			this.size = 10;
		} else {
			this.size = size;
		}
		this.categoryId = categoryId;
		this.lastArticleId = lastArticleId;
		this.searchText = searchText;
	}
}
