package com.sparta.instagramclone.repository;

import com.sparta.instagramclone.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscribeRepository extends JpaRepository<Follow, Long> {
}
