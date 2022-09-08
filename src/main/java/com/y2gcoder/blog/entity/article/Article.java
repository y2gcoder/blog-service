package com.y2gcoder.blog.entity.article;

import com.y2gcoder.blog.entity.category.Category;
import com.y2gcoder.blog.entity.common.BaseTimeEntity;
import com.y2gcoder.blog.entity.member.Member;
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

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	public Article(String title, String content, String thumbnailUrl, Category category, Member member) {
		this.title = title;
		this.content = content;
		this.thumbnailUrl = thumbnailUrl;
		this.category = category;
		this.member = member;
	}

	public void changeCategory(Category category) {
		this.category = category;
	}

	public void changeTitle(String title) {
		this.title = title;
	}

	public void changeContent(String content) {
		this.content = content;
	}

	public void changeThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
}
