package com.example.community.controller;

import com.example.community.entity.DiscussPost;
import com.example.community.entity.Page;
import com.example.community.entity.User;
import com.example.community.service.DiscussPostService;
import com.example.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author INSLYAB
 * @Date 2022/6/22 19:54
 */
@Controller
public class HomeController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;


    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page){
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");

        List<DiscussPost> discussPosts = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String, Object>> list = new ArrayList<>();
        if(discussPosts != null){
            for(DiscussPost discussPost : discussPosts){
                Map<String, Object> map = new HashMap<>();
                map.put("post",discussPost);
                User user = userService.findUserById(discussPost.getUserId());
                map.put("user", user);
                list.add(map);
            }
        }
        model.addAttribute("discussPosts", list);
        return "/index";
    }

}
