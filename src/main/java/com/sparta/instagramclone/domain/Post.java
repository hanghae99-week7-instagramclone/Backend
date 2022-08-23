package com.sparta.instagramclone.domain;

import com.sparta.instagramclone.dto.request.PostRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Like> likes;

    public void update(PostRequestDto postRequestDto, List<String> imgUrlList) {
        this.content = postRequestDto.getContent();
        if (imgUrlList != null) {
            this.imgUrlList = imgUrlList;
        }
    }

    public boolean validateMember(Member member) {
        return !this.member.getId().equals(member.getId());
    }
}
