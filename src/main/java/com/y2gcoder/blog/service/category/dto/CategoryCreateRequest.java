package com.y2gcoder.blog.service.category.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "카테고리 생성 요청")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreateRequest {
	@ApiModelProperty(value = "카테고리명", notes = "카테고리명을 입력해주세요.", required = true, example = "my category")
	@NotBlank(message = "카테고리명을 입력해주세요.")
	private String name;
}
