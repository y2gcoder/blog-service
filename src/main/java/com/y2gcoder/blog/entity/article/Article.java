package com.y2gcoder.blog.entity.article;

import com.y2gcoder.blog.entity.category.Category;
import com.y2gcoder.blog.entity.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Article extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String title;

	@Lob
	private String content;

	private String thumbnailUrl;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	public Article(String title, String content, String thumbnailUrl, Category category) {
		this.title = title;
		this.content = content;
		this.thumbnailUrl = thumbnailUrl;
		this.category = category;
	}

	public void changeCategory(Category category) {
		this.category = category;
	}
}
