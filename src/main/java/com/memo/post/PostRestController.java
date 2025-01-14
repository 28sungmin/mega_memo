package com.memo.post;

import com.memo.post.bo.PostBO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            HttpSession session
    ) {
        // 글쓴이 번호 꺼내기 (RequestParam이 아니라 session으로 하기!!)
        int userId = (int)session.getAttribute("userId");

        // db insert
        int rowCount = postBO.addPostByUserIdSubjectContent(userId, subject, content);

        // 응답값
        Map<String, Object> result = new HashMap<>();

        if (rowCount > 0) {
            result.put("code", 200);
            result.put("result", "성공");
        } else {
            result.put("code", 500);
            result.put("error_message", "글을 추가할 수 없습니다.");
        }

        return result;
    }
}
