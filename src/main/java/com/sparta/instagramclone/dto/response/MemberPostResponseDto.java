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
public class MemberPostResponseDto {
    private Long id;
    private String content;
    private List<String> imageUrlList;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
