package com.y2gcoder.blog.entity.comment;

import com.y2gcoder.blog.entity.article.Article;
import com.y2gcoder.blog.entity.common.BaseTimeEntity;
import com.y2gcoder.blog.entity.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String content;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne
	@JoinColumn(name = "article_id")
	private Article article;

	public Comment(String content, Member member, Article article) {
		this.content = content;
		this.member = member;
		this.article = article;
	}

	public void updateContent(String content) {
		this.content = content;
	}
}
