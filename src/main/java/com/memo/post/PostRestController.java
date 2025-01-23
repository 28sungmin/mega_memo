package com.memo.post;

import com.memo.post.bo.PostBO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/post")
@RestController
public class PostRestController {

    private final PostBO postBO;

    /**
     * 글쓰기 API
     * @param subject
     * @param content
     * @param file
     * @param session
     * @return
     */
    @PostMapping("/create")
    public Map<String, Object> create(
            @RequestParam("subject") String subject,
            @RequestParam("content") String content,
            @RequestParam(value = "file", required = false) MultipartFile file,
            HttpSession session
    ) {
        // 글쓴이 번호 꺼내기 (RequestParam이 아니라 session으로 하기!!)
        int userId = (int)session.getAttribute("userId");
        String userLoginId = (String)session.getAttribute("userLoginId");

        // db insert
        int rowCount = postBO.addPost(userId, userLoginId, subject, content, file);

        // 응답값
        Map<String, Object> result = new HashMap<>();

        if (rowCount > 0) {
            result.put("code", 200);
            result.put("result", "성공");
        } else {
            result.put("code", 500);
            result.put("error_message", "글쓰기에 실패했습니다.");
        }

        return result;
    }

    /**
     * 글 수정 API
     * @param postId
     * @param subject
     * @param content
     * @param file
     * @param session
     * @return
     */
    @PutMapping("/update")
    public Map<String, Object> update(
            @RequestParam("postId") int postId,
            @RequestParam("subject") String subject,
            @RequestParam("content") String content,
            @RequestParam(value = "file", required = false) MultipartFile file,
            HttpSession session
    ) {

        // 세션 => userId(테이블), userLoginId(폴더)
        int userId = (int)session.getAttribute("userId");
        String userLoginId = (String)session.getAttribute("userLoginId");

        // DB 업데이트, 파일 업로드
        postBO.updatePostByPostIdUserId(userId, userLoginId, postId, subject, content, file);

        // 응답값
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("result", "성공");

        return result;
    }

    @DeleteMapping("/delete")
    public Map<String, Object> delete(
            @RequestParam("postId") int postId,
            @RequestParam(value = "file", required = false) MultipartFile file,
            HttpSession session
    ) {
        // userId 가져오기
        int userId = (int)session.getAttribute("userId");

        Map<String, Object> result = new HashMap<>();
        // DB delete
        if (postBO.deletePostByPostIdUserId(postId, userId, file)) {
            result.put("code", 200);
            result.put("result", "성공");
        } else {
            result.put("code", 500);
            result.put("error_message", "삭제에 실패했습니다.");
        }

        return result;
    }

}
