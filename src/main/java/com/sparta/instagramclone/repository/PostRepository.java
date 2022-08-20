package com.sparta.instagramclone.repository;

import com.sparta.instagramclone.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
