package com.y2gcoder.blog.repository.comment;

import lombok.Data;

@Data
public class CommentCondition {
	private int size;
	private Long articleId;
	private Long lastCommentId;

	public CommentCondition() {
		this.size = 10;
	}

	public CommentCondition(int size, Long articleId, Long lastCommentId) {
		if (size <= 0) {
			this.size = 10;
		} else {
			this.size = size;
		}
		this.articleId = articleId;
		this.lastCommentId = lastCommentId;
	}
}
