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
    private List<String> imgUrlList;
    private String author;
    private String content;
//    private List<CommentResponseDto> commentResponseDto;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
