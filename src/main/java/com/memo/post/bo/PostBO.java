package com.memo.post.bo;

import com.memo.common.FileManagerService;
import com.memo.post.domain.Post;
import com.memo.post.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor // DI의 방식, 생성자 주입 방식 => 아래의 생성자를 만드는 과정을 해주는 어노테이션
@Service
public class PostBO {

    private final PostMapper postMapper;
    private final FileManagerService fileManager;

    // 이렇게 생성자를 만들면 너무 번거롭다. -> lombok의 어노테이션 이용
//    public PostBO(PostMapper postMapper) {
//        this.postMapper = postMapper;
//    }

    // i: userId
    // o: List<Post>
    public List<Post> getPostListByUserId(int userId) {
        return postMapper.selectPostListByUserId(userId);
    }

    // i: userId, userLoginId, subject, content, file
    // o: int(성공한 행의 개수)
    public int addPost(int userId, String userLoginId,
                       String subject, String content, MultipartFile file) {

        String imagePath = null;

        // 파일이 존재할 때 업로드를 하는 내용
        if (file != null) {
            imagePath = fileManager.uploadFile(file, userLoginId);
        }

        return postMapper.insertPost(userId, subject, content, imagePath);
    }
}
