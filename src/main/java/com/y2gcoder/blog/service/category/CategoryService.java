package com.y2gcoder.blog.service.category;

import com.y2gcoder.blog.entity.category.Category;
import com.y2gcoder.blog.repository.category.CategoryRepository;
import com.y2gcoder.blog.service.category.dto.CategoryCreateRequest;
import com.y2gcoder.blog.service.category.dto.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CategoryService {
	private final CategoryRepository categoryRepository;

	public List<CategoryDto> readAll() {
		return categoryRepository
				.findAll()
				.stream()
				.map(CategoryDto::new)
				.collect(Collectors.toList());
	}

	@Transactional
	public void create(CategoryCreateRequest req) {
		validateCategoryCreate(req);
		categoryRepository.save(new Category(req.getName()));
	}

	private void validateCategoryCreate(CategoryCreateRequest req) {
		if (categoryRepository.existsByName(req.getName())) {
			throw new IllegalArgumentException("해당 이름의 카테고리가 이미 존재합니다.");
		}
	}

	@Transactional
	public void delete(Long id) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("해당 카테고리를 찾을 수 없습니다. id=" + id));
		categoryRepository.delete(category);
	}
}
