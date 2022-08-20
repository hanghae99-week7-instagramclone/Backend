package com.sparta.instagramclone.dto.request;

import com.sparta.instagramclone.domain.Member;
import com.sparta.instagramclone.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeRequestDto {
    private Member member;
    private Post post;
}
