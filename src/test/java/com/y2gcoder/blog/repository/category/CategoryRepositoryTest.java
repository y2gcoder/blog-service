package com.y2gcoder.blog.repository.category;

import com.y2gcoder.blog.entity.category.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class CategoryRepositoryTest {
	@Autowired
	CategoryRepository categoryRepository;

	@Test
	@DisplayName("Category: 저장 성공")
	void saveCategory_Normal_Success() {
		//given
		Category category = createCategory();
		//when
		categoryRepository.save(category);
		//then
		assertThat(categoryRepository.findById(category.getId()).isPresent()).isTrue();
	}

	@Test
	@DisplayName("Category: 저장 실패, 중복 이름")
	void saveCategory_DuplicateName_Fail() {
		//given
		categoryRepository.save(createCategory());
		//when
		//then
		assertThatThrownBy(() -> categoryRepository.save(createCategory())).isInstanceOf(DataIntegrityViolationException.class);
	}

	private static Category createCategory() {
		return new Category("카테고리");
	}
}