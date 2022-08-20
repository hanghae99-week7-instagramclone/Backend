package com.sparta.instagramclone.repository;

import com.sparta.instagramclone.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<String> findImgUrlListById(Long postId);
}
