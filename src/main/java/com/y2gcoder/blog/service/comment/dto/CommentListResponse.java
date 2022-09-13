package com.y2gcoder.blog.service.comment.dto;

import com.y2gcoder.blog.repository.comment.dto.CommentDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter @Setter
public class CommentListResponse {
	private boolean hasNext;
	private List<CommentDto> content;


	public CommentListResponse(Slice<CommentDto> slice) {
		this.hasNext = slice.hasNext();
		this.content = slice.getContent();
	}
}
