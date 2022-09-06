package com.y2gcoder.blog.service.category;

import com.y2gcoder.blog.entity.category.Category;
import com.y2gcoder.blog.repository.category.CategoryRepository;
import com.y2gcoder.blog.service.category.dto.CategoryCreateRequest;
import com.y2gcoder.blog.service.category.dto.CategoryDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
	@InjectMocks CategoryService categoryService;
	@Mock
	CategoryRepository categoryRepository;

	@Test
	@DisplayName("카테고리: 모든 카테고리 조회 성공")
	void readAll_Normal_Success() {
		//given
		given(categoryRepository.findAll()).willReturn(
				List.of(
						new Category("category 1"),
						new Category("category 2")
				)
		);
		//when
		List<CategoryDto> result = categoryService.readAll();
		//then
		assertThat(result.size()).isEqualTo(2);
	}

	@Test
	@DisplayName("카테고리: 생성 성공")
	void create_Normal_Success() {
		//given
		CategoryCreateRequest req = createCategoryCreateRequest();
		//when
		categoryService.create(req);
		//then
		verify(categoryRepository).save(any());
	}

	@Test
	@DisplayName("카테고리: 생성 실패, 중복 카테고리명")
	void create_DuplicateName_Fail() {
		//given
		given(categoryRepository.existsByName(anyString())).willReturn(true);
		//when
		//then
		assertThatThrownBy(() -> categoryService.create(createCategoryCreateRequest()))
				.isInstanceOf(IllegalArgumentException.class);
	}

	private static CategoryCreateRequest createCategoryCreateRequest() {
		return new CategoryCreateRequest("category");
	}

	@Test
	@DisplayName("카테고리: 삭제 성공")
	void delete_Normal_Success() {
		//given
		given(categoryRepository.findById(anyLong())).willReturn(Optional.of(new Category("category")));
		//when
		categoryService.delete(1L);
		//then
		verify(categoryRepository).delete(any());
	}

	@Test
	@DisplayName("카테고리: 삭제 실패, 해당 카테고리 없음")
	void delete_NotFoundCategory_Fail() {
		//given
		given(categoryRepository.findById(anyLong())).willReturn(Optional.empty());
		//when
		//then
		assertThatThrownBy(() -> categoryService.delete(1L))
				.isInstanceOf(IllegalArgumentException.class);
	}

}