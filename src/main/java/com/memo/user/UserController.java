package com.memo.user;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
@Controller
public class UserController {

    /**
     * 회원가입 화면
     */
    @GetMapping("/sign-up-view")
    public String signUpView() {
        return "user/signUp";
    }

    /**
     * 로그인 화면
     */
    @GetMapping("/sign-in-view")
    public String signInView() {
        return "user/signIn";
    }

    @GetMapping("/sign-out")
    public String signOut(HttpSession session) {
        // session을 비운다.
        session.removeAttribute("userId");
        session.removeAttribute("userLoginId");
        session.removeAttribute("userName");

        // 화면으로 이동한다.(redirect)
        return "redirect:/user/sign-in-view";
    }
}
