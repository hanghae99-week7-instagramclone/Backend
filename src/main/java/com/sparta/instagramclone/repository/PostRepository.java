package com.sparta.instagramclone.repository;

import com.sparta.instagramclone.domain.Post;
import com.sparta.instagramclone.dto.response.PostInfiniteScrollResponseDto;
import com.sparta.instagramclone.dto.response.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    List<Post> findAllByOrderByCreatedAtDesc();
    List<Post> findAllByMember_Id(Long memberId);
    Long countByMemberId(Long memberId);

    @Override
    Page<PostInfiniteScrollResponseDto> getPostPaging(Pageable pageable);

    @Override
    Slice<PostInfiniteScrollResponseDto> getPostScroll(Pageable pageable);
}
