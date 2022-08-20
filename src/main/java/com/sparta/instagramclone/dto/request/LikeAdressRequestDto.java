package com.sparta.instagramclone.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LikeAdressRequestDto {
    private Long postId;
    private Long memberId;
}
