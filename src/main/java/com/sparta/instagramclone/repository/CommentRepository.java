package com.sparta.instagramclone.repository;

import com.sparta.instagramclone.domain.Comment;
import com.sparta.instagramclone.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);

    List<Comment> findAllByPostId(Long postId);
}

