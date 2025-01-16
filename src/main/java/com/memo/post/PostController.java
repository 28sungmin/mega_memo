package com.memo.post;

import com.memo.post.bo.PostBO;
import com.memo.post.domain.Post;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/post")
@Controller
public class PostController {

    private final PostBO postBO;

    @GetMapping("/post-list-view")
    public String postListView(Model model, HttpSession session) {
        // 로그인 된 사람인지 검사
        // 로그인 안 된 사람이 주소를 치고 들어와도 바로 로그인으로 가도록 하기 위해 Integer로 받을 거임
        Integer userId = (Integer)session.getAttribute("userId");
        if (userId == null) {
            // 비로그인이면 로그인 화면으로 이동시킨다.
            return "redirect:/user/sign-in-view";
        }

        // 데이터 가져오기
        List<Post> postList = postBO.getPostListByUserId(userId);

        // 모델에 담기
        model.addAttribute("postList", postList);

        return "post/postList";
    }

    /**
     * 글쓰기 화면
     * @return
     */
    @GetMapping("/post-create-view")
    public String postCreateView() {
        return "post/postCreate";
    }

    @GetMapping("/post-detail-view")
    public String postDetailView(
            @RequestParam("postId") int postId,
            Model model,
            HttpSession session
    ) {
        // db select - userId로 검사도 하고, postId 가져오기
        int userId = (int)session.getAttribute("userId");
        Post post = postBO.getPostByPostIdUserId(postId, userId);

        // model
        model.addAttribute("post", post);

        return "post/postDetail";
    }
}
