package com.sparta.instagramclone.dto.response;

import com.sparta.instagramclone.domain.Comment;
import com.sparta.instagramclone.domain.Like;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostInfiniteScrollResponseDto {
    private Long id;
    private String profileUrl;
    private String nickname;
    private Long authorId;
    private String content;
    private Boolean heartByMe;
    private List<String> imgUrlList;
    private Set<Comment> comments;
    private Set<Like> likes;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
