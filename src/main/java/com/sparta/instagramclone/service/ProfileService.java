package com.sparta.instagramclone.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.sparta.instagramclone.domain.Member;
import com.sparta.instagramclone.dto.request.ProfileRequestDto;
import com.sparta.instagramclone.dto.response.ProfileResponseDto;
import com.sparta.instagramclone.dto.response.ResponseDto;
import com.sparta.instagramclone.handler.ex.MemberNotFoundException;
import com.sparta.instagramclone.repository.MemberRepository;
import com.sparta.instagramclone.shared.Verification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    private final AmazonS3Client amazonS3Client;
    private final MemberRepository memberRepository;
    private final Verification verification;

    @Value("cloud.aws.s3.bucket")
    private String bucket;

    @Transactional
    public ResponseDto<?> updateProfile(Long memberId, ProfileRequestDto profileRequestDto, MultipartFile file, HttpServletRequest request) throws IOException {
        Member checkMember = verification.validateMember(request);
        verification.tokenCheck(request, checkMember);

        if (!checkMember.getId().equals(memberId)) {
            throw new IllegalArgumentException("자신의 프로필이 아닙니다.");
        }
        if (memberRepository.countByNickname(profileRequestDto.getNickname()) != 0) {
            return ResponseDto.fail("BAD_REQUEST", "존재하는 닉네임입니다.");
        }

        Member member = verification.getCurrentMember(memberId);

        String profileUrl;
        if (file != null) {
            if (member.getProfileUrl() != null) {
                String key = member.getProfileUrl().substring("https://mini-spring-bucket-team7.s3.ap-northeast-2.amazonaws.com/".length());
                amazonS3Client.deleteObject(bucket, key);
            }
            profileUrl = awsS3Service.upload(file);
            member.updateProfile(profileRequestDto, profileUrl);
        } else {
            profileUrl = member.getProfileUrl();
            member.updateProfile(profileRequestDto, profileUrl);
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
                    .createdAt(member.get().getCreatedAt())
                    .modifiedAt(member.get().getModifiedAt())
                    .build());

        }
        return ResponseDto.fail("MEMBER_NOT_FOUND", "사용자를 찾을 수 없습니다.");
    }
}
