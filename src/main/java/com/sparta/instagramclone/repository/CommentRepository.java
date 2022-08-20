package com.sparta.instagramclone.repository;

import com.sparta.instagramclone.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
        }
