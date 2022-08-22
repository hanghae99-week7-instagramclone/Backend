package com.sparta.instagramclone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private Long id;
    private String profileUrl;
    private String nickname;
    private String content;
    private Boolean heartByMe;
    private List<String> imgUrlList;
    private List<CommentResponseDto> commentResponseDto;
    private List<LikeResponseDto> likeResponseDto;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
