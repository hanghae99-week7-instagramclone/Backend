package com.sparta.instagramclone.repository;

import com.sparta.instagramclone.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import java.util.Optional;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByMember_Id(Long memberId);

    List<String> findImgUrlListById(Long postId);
}
