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
    public String postListView(
            @RequestParam(value = "prevId", required = false) Integer prevIdParam,
            @RequestParam(value = "nextId", required = false) Integer nextIdParam,
            Model model, HttpSession session) {
        // 로그인 된 사람인지 검사
        // 로그인 안 된 사람이 주소를 치고 들어와도 바로 로그인으로 가도록 하기 위해 Integer로 받을 거임
        Integer userId = (Integer)session.getAttribute("userId");
        if (userId == null) {
            // 비로그인이면 로그인 화면으로 이동시킨다.
            return "redirect:/user/sign-in-view";
        }

        // 데이터 가져오기
        List<Post> postList = postBO.getPostListByUserId(userId, prevIdParam, nextIdParam);
        int prevId = 0;
        int nextId = 0;

        if (postList.isEmpty() == false) { // postList가 비어있지 않을 때 페이징 정보를 채운다.
            prevId = postList.get(0).getId(); // 첫 번째 칸의 postId를 넣기
            nextId = postList.get(postList.size() - 1).getId(); // 마지막 칸의 postId를 넣기

            // 이전이 없나? 그렇다면 0으로 하기
            // user가 쓴 글들 중 제일 큰 숫자가 prevId와 같으면 이전이 없는 것임
            if (postBO.isPrevLastPageByUserId(userId, prevId)) {
                prevId = 0;
            }

            // 다음이 없나? 그렇다면 0으로 하기
            // user가 쓴 글들 중 제일 작은 숫자가 nextId와 같으면 다음이 없는 것임
            if (postBO.isNextLastPageByUserId(userId, nextId)) {
                nextId = 0;
            }
        }

        // 모델에 담기
        model.addAttribute("prevId", prevId);
        model.addAttribute("nextId", nextId);
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

    /**
     * 글 상세 확인 화면
     * @param postId
     * @param model
     * @param session
     * @return
     */
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
