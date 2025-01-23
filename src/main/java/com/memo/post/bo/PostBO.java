package com.memo.post.bo;

import com.memo.common.FileManagerService;
import com.memo.post.domain.Post;
import com.memo.post.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor // DI의 방식, 생성자 주입 방식 => 아래의 생성자를 만드는 과정을 해주는 어노테이션
@Service
public class PostBO {

    private final PostMapper postMapper;
    private final FileManagerService fileManager;
    // PostBO 객체에 관한 내용을 로깅할 것이다. => 롬복으로 할 수도 있다.
//    private Logger log = LoggerFactory.getLogger(PostBO.class);
//    private Logger log = LoggerFactory.getLogger(this.getClass());

    // 이렇게 생성자를 만들면 너무 번거롭다. -> lombok의 어노테이션 이용
//    public PostBO(PostMapper postMapper) {
//        this.postMapper = postMapper;
//    }

    // i: userId
    // o: List<Post>
    public List<Post> getPostListByUserId(int userId) {
        return postMapper.selectPostListByUserId(userId);
    }

    // i: postId, userId
    // o: Post or null
    public Post getPostByPostIdUserId(int postId, int userId) {
        return postMapper.selectPostByPostIdUserId(postId, userId);
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

    public void updatePostByPostIdUserId(int userId, String userLoginId,
                                         int postId, String subject, String content,
                                         MultipartFile file) {

        // 기존글 가져오기
        // 이유1. 이미지 교체시 기존 이미지 제거를 위해 - 여기서의 강력한 이유
        // 이유2. 대상 존재 확인을 위해 - 생략 가능
        Post post = postMapper.selectPostByPostIdUserId(postId, userId);
        if (post == null) {
            log.warn("[### 글 수정] post is null. postId:{}, userId:{}", postId, userId);
            return;
        }

        // 파일 존재 시 이미지 업로드
        String imagePath = null;
        if (file != null) {
            // 새 이미지 업로드
            imagePath = fileManager.uploadFile(file, userLoginId);

            // imagePath가 있으면(성공이면) 이고, 기존 이미지가 있다면 기존 이미지를 삭제
            if (imagePath != null && post.getImagePath() != null) {
                // 폴더와 이미지 삭제(컴퓨터 서버에 있는)
                fileManager.deleteFile(post.getImagePath());
            }
        }

        // DB update
        // 수정
        // 업로드 할 이미지가 없는 경우 -> 기존 이미지 존재시 유지 => imagePath가 null인 경우
        // 업로드 할 이미지가 있는 경우 -> 성공 -> 기존 이미지 제거 => imagePath 가 있으면 업데이트
        // 업로드 할 이미지가 있는 경우 -> 실패 -> 기존 이미지 존재시 유지 => imagePath가 null인 경우
        // imagePath는 null이거나 있다. 분기는 mapper 쿼리에서 한다.
        postMapper.updatePostById(postId, subject, content, imagePath);
    }

    public boolean deletePostByPostIdUserId(int postId, int userId,
                                            MultipartFile file) {

        // 기존 글 가져오기
        Post post = postMapper.selectPostByPostIdUserId(postId, userId);
        if (post == null) {
            log.warn("[### 글 삭제] post is null. postId:{}, userId:{}", postId, userId);
            return false;
        }

        // 이미지가 있으면 폴더와 이미지 삭제
        if (file != null) {
            fileManager.deleteFile(post.getImagePath());
        }

        return postMapper.deletePostByPostIdUserId(postId) > 0;
    }

}
