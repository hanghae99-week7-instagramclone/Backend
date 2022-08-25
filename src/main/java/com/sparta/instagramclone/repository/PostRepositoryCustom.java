package com.sparta.instagramclone.repository;

import com.sparta.instagramclone.dto.response.PostInfiniteScrollResponseDto;
import com.sparta.instagramclone.dto.response.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import javax.servlet.http.HttpServletRequest;

public interface PostRepositoryCustom {
    Page<PostInfiniteScrollResponseDto> getPostPaging(Pageable pageable);
    Slice<PostInfiniteScrollResponseDto> getPostScroll(Pageable pageable);
}
