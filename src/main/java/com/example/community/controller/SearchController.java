package com.example.community.controller;

import com.example.community.entity.DiscussPost;
import com.example.community.entity.Page;
import com.example.community.service.ElasticsearchService;
import com.example.community.service.LikeService;
import com.example.community.service.UserService;
import com.example.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
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
 * @Date 2022/7/18 11:15
 */
@Controller
public class SearchController implements CommunityConstant {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public String search(String keyword, Page page, Model model){
        SearchHits<DiscussPost> searchHits = elasticsearchService.searchDiscussPost(keyword, page.getCurrent() - 1, page.getLimit());
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if(searchHits != null){
            for(SearchHit<DiscussPost> searchHit : searchHits){
                Map<String, Object> map = new HashMap<>();
                DiscussPost post = searchHit.getContent();
                map.put("post", post);
                map.put("user", userService.findUserById(post.getUserId()));
                map.put("likeCount", likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId()));
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
        model.addAttribute("keyword", keyword);
        page.setPath("/search?keyword=" + keyword);
        page.setRows(searchHits == null ? 0 : (int) searchHits.getTotalHits());
        return "/site/search";
    }
}
