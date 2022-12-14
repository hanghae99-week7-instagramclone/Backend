package com.sparta.instagramclone.dto.response;

import com.sparta.instagramclone.domain.Comment;
import com.sparta.instagramclone.domain.Like;
import com.sparta.instagramclone.domain.Post;
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
public class PostResponseDto {
    private Long id;
    private String profileUrl;
    private String nickname;
    private Long authorId;
    private String content;
    private Boolean heartByMe;
    private List<String> imgUrlList;
    private List<CommentResponseDto> commentResponseDto;
    private List<LikeResponseDto> likeResponseDto;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
