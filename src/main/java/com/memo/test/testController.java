package com.memo.test;

import com.memo.post.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class testController {

    @Autowired
    private PostMapper postMapper;

    @GetMapping("/test1")
    @ResponseBody
    public String test1() {
        return "<h2>Hello world</h2>";
    }

    @GetMapping("/test2")
    @ResponseBody
    public Map<String, Object> test2() {
        Map<String, Object> map = new HashMap<>();
        map.put("사과", 111);
        map.put("포도", 23);
        map.put("배", 156);

        return map;
    }

    @GetMapping("/test3")
    public String test3() {
        return "test/test3";
    }

    @GetMapping("/test4")
    @ResponseBody
    List<Map<String, Object>> test4() {
        return postMapper.selectPostListTest();
    }
}
