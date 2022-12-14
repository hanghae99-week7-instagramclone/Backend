package com.sparta.instagramclone.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.instagramclone.domain.Like;
import com.sparta.instagramclone.domain.Member;
import com.sparta.instagramclone.domain.Post;
import com.sparta.instagramclone.domain.QPost;
import com.sparta.instagramclone.dto.response.PostInfiniteScrollResponseDto;
import com.sparta.instagramclone.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final LikeRepository likeRepository;
    private final JwtTokenProvider tokenProvider;
    @Override
    public Page<PostInfiniteScrollResponseDto> getPostPaging(Pageable pageable) {
        return null;
    }

    @Override
    public Slice<PostInfiniteScrollResponseDto> getPostScroll(Pageable pageable) {
        QPost post = QPost.post;
        Member member = tokenProvider.getMemberFromAuthentication();
        List<Post> results = queryFactory
                .select(post)
                .from(post)
                .orderBy(PostSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()+1)
                .fetch();
        List<PostInfiniteScrollResponseDto> content = new ArrayList<>();

        for (Post eachPost : results) {
            Optional<Like> likes = likeRepository.findByMemberAndPost(member, eachPost);
            boolean heartByMe;
            heartByMe = likes.isPresent();

            content.add(
                    PostInfiniteScrollResponseDto.builder()
                            .id(eachPost.getId())
                            .nickname(eachPost.getMember().getNickname())
                            .authorId(eachPost.getMember().getId())
                            .profileUrl(eachPost.getMember().getProfileUrl())
                            .content(eachPost.getContent())
                            .imgUrlList(eachPost.getImgUrlList())
                            .heartByMe(heartByMe)
                            .comments(eachPost.getComments())
                            .likes(eachPost.getLikes())
                            .createdAt(eachPost.getCreatedAt())
                            .modifiedAt(eachPost.getModifiedAt())
                            .build()
            );
        }
        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(content, pageable, hasNext);
    }

    private OrderSpecifier<?> PostSort(Pageable page) {
        QPost post = QPost.post;

        //??????????????? ????????? Pageable ????????? ???????????? null ??? ??????
        if (!page.getSort().isEmpty()) {
            //???????????? ?????? ????????? for ???????????? ?????? ????????????
            for (Sort.Order order : page.getSort()) {
                // ??????????????? ????????? DESC or ASC ??? ????????????.
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                // ??????????????? ????????? ?????? ????????? ????????? ????????? ?????? ???????????? ???????????? ??????.
                switch (order.getProperty()){
                    case "createdAt" :
                    case "descending":
                        return new OrderSpecifier(direction, post.createdAt);
                }
            }
        }
        return null;
    }

}
