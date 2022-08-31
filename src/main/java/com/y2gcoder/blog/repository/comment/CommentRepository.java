package com.y2gcoder.blog.repository.comment;

import com.y2gcoder.blog.entity.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
