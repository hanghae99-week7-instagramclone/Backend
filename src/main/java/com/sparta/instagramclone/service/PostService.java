package com.sparta.instagramclone.service;

import com.sparta.instagramclone.domain.Comment;
import com.sparta.instagramclone.domain.Like;
import com.sparta.instagramclone.domain.Member;
import com.sparta.instagramclone.domain.Post;
import com.sparta.instagramclone.dto.request.PostRequestDto;
import com.sparta.instagramclone.dto.response.*;
import com.sparta.instagramclone.handler.ex.NotAuthorException;
import com.sparta.instagramclone.repository.CommentRepository;
import com.sparta.instagramclone.repository.LikeRepository;
import com.sparta.instagramclone.repository.PostRepository;
import com.sparta.instagramclone.shared.Verification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final AwsS3Service awsS3Service;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final Verification verification;

    @Transactional
    public ResponseDto<?> createPost(List<MultipartFile> multipartFile, PostRequestDto postRequestDto, HttpServletRequest request) throws IOException {
        Member member = verification.validateMember(request);
        verification.tokenCheck(request, member);

        List<String> imgUrlList = new ArrayList<>();
        for (MultipartFile imgFile : multipartFile) {
            String fileName = awsS3Service.upload(imgFile);
            String imgUrl = URLDecoder.decode(fileName, "UTF-8");
            if (imgUrl.equals("false")) {
                return ResponseDto.fail("NOT_IMAGE_FILE", "이미지 파일만 업로드 가능합니다.");
            }
            imgUrlList.add(imgUrl);
        }

        Post post = Post.builder()
                .imgUrlList(imgUrlList)
                .content(postRequestDto.getContent())
                .member(member)
                .build();
        postRepository.save(post);

        return ResponseDto.success(
                PostResponseDto.builder()
                        .id(post.getId())
                        .nickname(post.getMember().getNickname())
                        .imgUrlList(post.getImgUrlList())
                        .content(post.getContent())
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .build()
        );
    }

    //유저 게시물 조회
    @Transactional
    public ResponseDto<?> getMemberPost(Long memberId){
        List<Post> postList = postRepository.findAllByMember_Id(memberId);
        List<MemberPostResponseDto> memberPostResponseDtoList = new ArrayList<>();
        for(Post post : postList){
            memberPostResponseDtoList.add(
                    MemberPostResponseDto.builder()
                            .id(post.getId())
                            .content(post.getContent())
                            .imageUrlList(post.getImgUrlList())
                            .createdAt(post.getCreatedAt())
                            .modifiedAt((post.getModifiedAt()))
                            .build());
        }
        return ResponseDto.success(memberPostResponseDtoList);
    }

    //게시물 상세 조회
    @Transactional
    public ResponseDto<?> getDetailPost(Long postId, HttpServletRequest request){
        Post post = verification.getCurrentPost(postId);
        verification.checkPost(post);
        List<Comment> commentList = commentRepository.findAllByPostId(postId);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            commentResponseDtoList.add(CommentResponseDto.builder()
                    .id(comment.getId())
                    .content(comment.getContent())
                    .createdAt(comment.getCreatedAt())
                    .modifiedAt(comment.getModifiedAt())
                    .postId(comment.getPost().getId())
                    .memberId(comment.getMember().getId())
                    .nickname(comment.getMember().getNickname())
                    .build());
        }

        Member member = verification.validateMember(request);
        if (null == member) {
            return ResponseDto.success(PostResponseDto.builder()
                    .id(post.getId())
                    .imgUrlList(post.getImgUrlList())
                    .nickname(post.getMember().getNickname())
                    .content(post.getContent())
                    .createdAt(post.getCreatedAt())
                    .modifiedAt(post.getModifiedAt())
                    .commentResponseDto(commentResponseDtoList)
                    .build());
        }
        Optional<Like> likes = likeRepository.findByMemberAndPost(member, post);
        boolean heartByMe;
        heartByMe = likes.isPresent();
        return ResponseDto.success(PostResponseDto.builder()
                .id(post.getId())
                .imgUrlList(post.getImgUrlList())
                .nickname(post.getMember().getNickname())
                .content(post.getContent())
                .heartByMe(heartByMe)
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .commentResponseDto(commentResponseDtoList)
                .build());
    }

    // 전체 게시물 조회
    @Transactional(readOnly = true)
    public ResponseDto<?> getAllPosts(HttpServletRequest request) {
        List<Post> postList = postRepository.findAllByOrderByCreatedAtDesc();
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        Member member = verification.validateMember(request);

        for (Post post : postList) {
            List<Comment> commentList = commentRepository.findAllByPost(post);
            List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
            for (Comment comment : commentList) {
                commentResponseDtoList.add(
                        CommentResponseDto.builder()
                                .id(comment.getId())
                                .nickname(comment.getMember().getNickname())
                                .content(comment.getContent())
                                .postId(comment.getPost().getId())
                                .memberId(comment.getMember().getId())
                                .createdAt(comment.getCreatedAt())
                                .modifiedAt(comment.getModifiedAt())
                                .build()
                );
            }
            List<Like> likeList = likeRepository.findByPost(post);
            List<LikeResponseDto> likeResponseDtoList = new ArrayList<>();
            for (Like like : likeList) {
                likeResponseDtoList.add(
                        LikeResponseDto.builder()
                                .postId(like.getPost().getId())
                                .nickname(like.getMember().getNickname())
                                .build()
                );
            }
            Optional<Like> likes = likeRepository.findByMemberAndPost(member, post);
            boolean heartByMe;
            heartByMe = likes.isPresent();
            postResponseDtoList.add(
                    PostResponseDto.builder()
                            .id(post.getId())
                            .nickname(post.getMember().getNickname())
                            .profileUrl(post.getMember().getProfileUrl())
                            .content(post.getContent())
                            .heartByMe(heartByMe)
                            .imgUrlList(post.getImgUrlList())
                            .commentResponseDto(commentResponseDtoList)
                            .likeResponseDto(likeResponseDtoList)
                            .createdAt(post.getCreatedAt())
                            .modifiedAt(post.getModifiedAt())
                            .build()
            );
        }
        return ResponseDto.success(postResponseDtoList);
    }

    @Transactional
    public ResponseDto<?> updatePost(Long postId, List<MultipartFile> multipartFile, PostRequestDto postRequestDto, HttpServletRequest request) throws IOException {
        Member member = verification.validateMember(request);
        Post post = verification.getCurrentPost(postId);
        verification.checkPost(post);
        verification.checkPostAuthor(member, post);

        List<String> imgUrlList = new ArrayList<>();

        if (multipartFile != null) {
            for (MultipartFile imgFile : multipartFile) {
                String imgUrl = awsS3Service.upload(imgFile);
                if (imgUrl.equals("false")) {
                    throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
                }
                imgUrlList.add(imgUrl);
                post.update(postRequestDto, imgUrlList);
            }
        } else {
            post.update(postRequestDto, null);
        }
        return ResponseDto.success(
                PostResponseDto.builder()
                        .id(post.getId())
                        .nickname(post.getMember().getNickname())
                        .imgUrlList(post.getImgUrlList())
                        .content(post.getContent())
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> deletePost(Long postId, HttpServletRequest request) {
        Member member = verification.validateMember(request);
        Post post = verification.getCurrentPost(postId);
        verification.checkPost(post);
        verification.checkPostAuthor(member, post);


        List<String> deleteImgList = post.getImgUrlList();
        for (String img : deleteImgList) {
            log.info(img);
            String key = img.substring("https://mini-spring-bucket-team7.s3.ap-northeast-2.amazonaws.com/".length());
            awsS3Service.deleteS3(key);
        }

        postRepository.delete(post);
        return ResponseDto.success("delete success");
    }

}
