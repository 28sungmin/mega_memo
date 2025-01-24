package com.memo.post.mapper;

import com.memo.post.domain.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

@Mapper
public interface PostMapper {
    public List<Map<String, Object>> selectPostListTest();

    public List<Post> selectPostListByUserId(
            @Param("userId") int userId,
            @Param("direction") String direction,
            @Param("standardId") Integer standardId,
            @Param("limit") int limit);

    public int selectPostIdByUserIdAsSort(
            @Param("userId") int userId,
            @Param("sort") String sort);

    public Post selectPostByPostIdUserId(
            @Param("postId") int postId,
            @Param("userId") int userId);

    public int insertPost(
            @Param("userId") int userId,
            @Param("subject") String subject,
            @Param("content") String content,
            @Param("imagePath") String imagePath);

    public void updatePostById(
            @Param("postId") int postId,
            @Param("subject") String subject,
            @Param("content") String content,
            @Param("imagePath") String imagePath);

    public int deletePostById(int postId);
}
