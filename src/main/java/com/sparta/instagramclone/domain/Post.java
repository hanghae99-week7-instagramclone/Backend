package com.sparta.instagramclone.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.instagramclone.dto.request.LikeRequestDto;
import com.sparta.instagramclone.dto.request.PostRequestDto;
import com.sparta.instagramclone.dto.response.CommentResponseDto;
import com.sparta.instagramclone.dto.response.LikeResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 다중 이미지
    @Column(columnDefinition = "mediumblob")
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "post_img_url_list",
            joinColumns = @JoinColumn(name = "post_id")
    )
    private List<String> imgUrlList;

    private String content;

    // 유저
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Like> likes;

    public void update(PostRequestDto postRequestDto, List<String> imgUrlList) {
        this.content = postRequestDto.getContent();
        if (imgUrlList != null) {
            this.imgUrlList = imgUrlList;
        }
    }

}
