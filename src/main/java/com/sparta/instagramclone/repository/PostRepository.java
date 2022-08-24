package com.sparta.instagramclone.repository;

import com.sparta.instagramclone.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import java.util.Optional;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long> {
    Slice<Post> findAllByIdLessThanOrderByCreatedAtDesc(Long lastPostId, Pageable pageable);
    List<Post> findAllByMember_Id(Long memberId);
    Long countByMemberId(Long memberId);

}
