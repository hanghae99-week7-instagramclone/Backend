package com.sparta.instagramclone.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.sparta.instagramclone.domain.Member;
import com.sparta.instagramclone.dto.request.ProfileRequestDto;
import com.sparta.instagramclone.dto.response.ProfileResponseDto;
import com.sparta.instagramclone.dto.response.ResponseDto;
import com.sparta.instagramclone.handler.ex.DuplicateNicknameException;
import com.sparta.instagramclone.handler.ex.MemberNotFoundException;
import com.sparta.instagramclone.repository.FollowRepository;
import com.sparta.instagramclone.repository.MemberRepository;
import com.sparta.instagramclone.repository.PostRepository;
import com.sparta.instagramclone.shared.Verification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {
    private final AwsS3Service awsS3Service;
    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final PostRepository postRepository;
    private final Verification verification;

    @Transactional
    public ResponseDto<?> updateProfile(Long memberId, ProfileRequestDto profileRequestDto, MultipartFile file, HttpServletRequest request) throws IOException {
        Member checkMember = verification.validateMember(request);
        verification.tokenCheck(request, checkMember);

        if (!checkMember.getId().equals(memberId)) {
            throw new IllegalArgumentException("자신의 프로필이 아닙니다.");
        }
        if (memberRepository.countByNickname(profileRequestDto.getNickname()) != 0) {
            throw new DuplicateNicknameException();
        }

        Member member = verification.getCurrentMember(memberId);

        String profileUrl;
        if (file != null) {
            if (member.getProfileUrl() != null) {
                String key = member.getProfileUrl().substring("https://mini-spring-bucket-team7.s3.ap-northeast-2.amazonaws.com/".length());
                awsS3Service.deleteS3(key);
            }
            profileUrl = awsS3Service.upload(file);
            if (profileUrl.equals("false")) {
                return ResponseDto.fail("NOT_IMAGE_FILE", "이미지 파일만 업로드 가능합니다.");
            }
            member.updateProfile(profileRequestDto, profileUrl);
        } else {
            member.updateProfile(profileRequestDto, null);
        }

        return ResponseDto.success(ProfileResponseDto.builder()
                .bio(member.getBio())
                .createdAt(member.getCreatedAt())
                .id(member.getId())
                .email(member.getEmail())
                .modifiedAt(member.getModifiedAt())
                .profileUrl(member.getProfileUrl())
                .nickname(member.getNickname())
                .username(member.getUsername())
                .websiteUrl(member.getWebsiteUrl())
                .build());
    }

    @Transactional
    public ResponseDto<?> getProfile(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        if (member.isPresent()) {
            return ResponseDto.success(ProfileResponseDto.builder()
                    .id(member.get().getId())
                    .bio(member.get().getBio())
                    .email(member.get().getEmail())
                    .profileUrl(member.get().getProfileUrl())
                    .nickname(member.get().getNickname())
                    .username(member.get().getUsername())
                    .websiteUrl(member.get().getWebsiteUrl())
                    .postCount(postRepository.countByMemberId(memberId))
                    .follower(followRepository.countFromMemberIdByToMemberId(memberId))
                    .follow(followRepository.countToMemberIdByFromMemberId(memberId))
                    .createdAt(member.get().getCreatedAt())
                    .modifiedAt(member.get().getModifiedAt())
                    .build());

        }
        throw new MemberNotFoundException();
    }
}
