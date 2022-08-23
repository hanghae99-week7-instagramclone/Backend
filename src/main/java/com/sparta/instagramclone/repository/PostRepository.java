package com.sparta.instagramclone.repository;

import com.sparta.instagramclone.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import java.util.Optional;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByModifiedAtDesc();
    List<Post> findAllByMember_Id(Long memberId);
    Long countByMemberId(Long memberId);
}
