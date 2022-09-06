package com.y2gcoder.blog.service.category.dto;

import com.y2gcoder.blog.entity.category.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
	private Long id;
	private String name;

	public CategoryDto(Category category) {
		this.id = category.getId();
		this.name = category.getName();
	}
}
