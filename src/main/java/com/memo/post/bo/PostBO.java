package com.memo.post.bo;

import com.memo.post.domain.Post;
import com.memo.post.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor // DI의 방식, 생성자 주입 방식 => 아래의 생성자를 만드는 과정을 해주는 어노테이션
@Service
public class PostBO {

    private final PostMapper postMapper;

    // 이렇게 생성자를 만들면 너무 번거롭다. -> lombok의 어노테이션 이용
//    public PostBO(PostMapper postMapper) {
//        this.postMapper = postMapper;
//    }

    // i: userId
    // o: List<Post>
    public List<Post> getPostListByUserId(int userId) {
        return postMapper.selectPostListByUserId(userId);
    }

    // i: userId, subject, content
    // o: int
    public int addPostByUserIdSubjectContent(int userId, String subject, String content) {
        return postMapper.insertPostByUserIdSubjectContent(userId, subject, content);
    }
}
