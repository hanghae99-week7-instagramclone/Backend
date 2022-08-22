package com.sparta.instagramclone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class DetailPostResponseDto {
    private Long id;
    private List<String> imgUrlList;
    private String author;
    private String content;
    private Boolean heartByMe;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> commentResponseDtoList;
}
