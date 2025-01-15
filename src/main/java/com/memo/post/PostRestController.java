package com.memo.post;

import com.memo.post.bo.PostBO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/post")
@RestController
public class PostRestController {

    private final PostBO postBO;

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
}
