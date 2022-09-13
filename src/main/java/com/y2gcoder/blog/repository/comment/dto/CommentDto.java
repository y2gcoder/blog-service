package com.y2gcoder.blog.repository.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.y2gcoder.blog.entity.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
	private Long id;
	private String content;
	private Commenter commenter;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime createdAt;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime updatedAt;

	public CommentDto(Comment comment) {
		this.id = comment.getId();
		this.content = comment.getContent();
		this.commenter = new Commenter(comment.getMember().getId(), comment.getMember().getEmail());
		this.createdAt = comment.getCreatedAt();
		this.updatedAt = comment.getUpdatedAt();
	}


	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Commenter {
		private Long id;
		private String email;
	}
}
