package com.example.community.controller;

import com.example.community.annotation.LoginRequired;
import com.example.community.entity.Comment;
import com.example.community.entity.DiscussPost;
import com.example.community.entity.Page;
import com.example.community.entity.User;
import com.example.community.service.*;
import com.example.community.util.CommunityConstant;
import com.example.community.util.CommunityUtil;
import com.example.community.util.HostHolder;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author INSLYAB
 * @Date 2022/6/29 12:10
 */
@Controller
@RequestMapping("/user")
public class UserController implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${qiniu.key.access}")
    private String accessKey;

    @Value("${qiniu.key.secret}")
    private String secretKey;

    @Value("${qiniu.bucket.header.name}")
    private String headerBucketName;

    @Value("${qiniu.bucket.header.url}")
    private String headerBucketUrl;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSettingPage(Model model){
        String fileName = CommunityUtil.generateUUID();
        StringMap policy = new StringMap();
        policy.put("returnBody",CommunityUtil.getJsonString(200));
        //生成凭证信息
        Auth auth = Auth.create(accessKey, secretKey);
        String uploadToken = auth.uploadToken(headerBucketName, fileName, 3600, policy);
        model.addAttribute("uploadToken", uploadToken);
        model.addAttribute("fileName", fileName);
        return "/site/setting";
    }

    @RequestMapping(path = "/header/url", method = RequestMethod.POST)
    @ResponseBody
    public String updateHeaderUrl(String fileName){
        if(StringUtils.isBlank(fileName)){
            return CommunityUtil.getJsonString(500, "文件名不能为空");
        }
        String url = headerBucketUrl + "/" + fileName;
        userService.updateHeader(hostHolder.getUser().getId(), url);
        return CommunityUtil.getJsonString(200,"更换头像成功");
    }

    //废弃
    @LoginRequired
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model){
        if(headerImage == null){
            model.addAttribute("error","您还没有选择图片");
            return "/site/setting";
        }
        String fileName = headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if(StringUtils.isBlank(suffix)){
            model.addAttribute("error","文件格式不正确");
            return "/site/setting";
        }
        fileName = CommunityUtil.generateUUID() + suffix;
        File dest = new File(uploadPath + "/" +fileName);
        try {
            //存储文件
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败: " + e.getMessage());
            throw new RuntimeException("上传文件失败，服务器异常" + e.getMessage());
        }
        //更新用户头像路径
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeader(user.getId(), headerUrl);
        return "redirect:/index";
    }

    //废弃
    @RequestMapping(value = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName,
                          HttpServletResponse response){
        fileName = uploadPath + "/" + fileName;
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        response.setContentType("image/" + suffix);
        try(FileInputStream fis = new FileInputStream(fileName);
            ServletOutputStream os = response.getOutputStream();) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while((b = fis.read(buffer)) != -1){
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败：" + e.getMessage());
        }
    }

    @LoginRequired
    @RequestMapping(path = "/updatePassword", method = RequestMethod.POST)
    public String updatePassword(String newPassword, String oldPassword, Model model){
        User user = hostHolder.getUser();
        Map<String, Object> map = userService.updatePassword(oldPassword, newPassword, user);
        if(map.containsKey("passwordMsg")){
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            return "/site/setting";
        }
        return "redirect:/logout";
    }

    @RequestMapping(path = "/profile/{userId}", method = RequestMethod.GET)
    public String getProfilePage(@PathVariable("userId") int userId, Model model){
        User user = userService.findUserById(userId);
        if(user == null){
            throw new RuntimeException("该用户不存在");
        }
        model.addAttribute("user", user);
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount",likeCount);
        long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount",followeeCount);
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount",followerCount);
        boolean hasFollowed = false;
        if(hostHolder.getUser() != null){
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
        }
        model.addAttribute("hasFollowed",hasFollowed);
        model.addAttribute("profile",true);
        return "/site/profile";
    }


    @RequestMapping(path = "/myPost/{userId}", method = RequestMethod.GET)
    public String getMyPostPage(Model model, Page page,
                               @PathVariable(name = "userId") int userId){
        page.setLimit(5);
        page.setRows(discussPostService.findDiscussPostRows(userId));
        page.setPath("/user/myPost/" + userId);

        List<DiscussPost> discussPosts = discussPostService.findDiscussPosts(userId, page.getOffset(), page.getLimit(), 0);
        List<Map<String, Object>> list = new ArrayList<>();
        if(discussPosts != null){
            for(DiscussPost discussPost : discussPosts){
                Map<String, Object> map = new HashMap<>();
                map.put("post",discussPost);
                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, discussPost.getId());
                map.put("likeCount", likeCount);
                list.add(map);
            }
        }
        model.addAttribute("discussPosts", list);
        model.addAttribute("rows",page.getRows());
        model.addAttribute("myPost",true);
        return "/site/my-post";
    }

    @RequestMapping(path = "/myReply/{userId}", method = RequestMethod.GET)
    public String getMyReplyPage(Model model, Page page,
                               @PathVariable(name = "userId") int userId){
        page.setLimit(5);
        page.setRows(commentService.findCommentCountByUserId(userId));
        page.setPath("/user/myReply/" + userId);

        List<Comment> comments = commentService.findCommentByUserId(userId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> list = new ArrayList<>();
        if(comments != null){
            for(Comment comment : comments){
                Map<String, Object> map = new HashMap<>();
                DiscussPost discussPost = discussPostService.findDiscussPostById(comment.getEntityId());
                map.put("post",discussPost);
                map.put("comment", comment);
                list.add(map);
            }
        }
        model.addAttribute("comments", list);
        model.addAttribute("rows",page.getRows());
        model.addAttribute("myReply",true);
        return "/site/my-reply";
    }
}
