package com.sparta.instagramclone.repository;

import com.sparta.instagramclone.domain.Follow;
import com.sparta.instagramclone.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFromMemberAndToMember_Id(Member member, Long postId);
}
