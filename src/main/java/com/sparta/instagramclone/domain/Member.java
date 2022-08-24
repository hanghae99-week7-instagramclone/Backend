package com.sparta.instagramclone.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sparta.instagramclone.dto.request.ProfileRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Objects;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"createdAt", "modifiedAt"})
public class Member extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Email
    @JsonIgnore
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    @JsonIgnore
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column
    @JsonIgnore
    private String profileUrl;

    @Column
    @JsonIgnore
    private String bio;

    @Column
    @JsonIgnore
    private String websiteUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Member member = (Member) o;
        return id != null && Objects.equals(id, member.id);
    }

    public void updateProfile(ProfileRequestDto profileRequestDto, String profileUrl) {
        if (profileRequestDto.getUsername() != null) {
            this.username = profileRequestDto.getUsername();
        }
        if (profileRequestDto.getNickname() != null) {
            this.nickname = profileRequestDto.getNickname();
        }
        if (profileUrl != null) {
            this.profileUrl = profileUrl;
        }
        if (profileRequestDto.getWebsiteUrl() != null) {
            this.websiteUrl = profileRequestDto.getWebsiteUrl();
        }
        if (profileRequestDto.getBio() != null) {
            this.bio = profileRequestDto.getBio();
        }
    }

}
