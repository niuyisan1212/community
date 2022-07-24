package com.example.community.controller;

import com.example.community.entity.User;
import com.example.community.service.UserService;
import com.example.community.util.CommunityConstant;
import com.example.community.util.CommunityUtil;
import com.example.community.util.RedisKeyUtil;
import com.google.code.kaptcha.Producer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author INSLYAB
 * @Date 2022/6/25 16:14
 */
@Controller
public class LoginController implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private Producer kaptchaProducer;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${server.servlet.context-path}")
    private String CONTEXT_PATH;

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String getRegisterPage(){
        return "/site/register";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getLoginPage(){
        return "/site/login";
    }

    @RequestMapping(path = "/forget", method = RequestMethod.GET)
    public String getForgetPage(){
        return "/site/forget";
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(Model model, User user){
        Map<String, Object> map = userService.register(user);
        if(map == null || map.isEmpty()){
            model.addAttribute("msg","注册成功，已经向您的邮箱发送了一封激活邮件，请尽快激活!");
            model.addAttribute("target","/index");
            return "/site/operate-result";
        }else{
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            model.addAttribute("emailMsg",map.get("emailMsg"));
            return "/site/register";
        }
    }

    @RequestMapping(path = "/activation/{userId}/{code}", method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code){
        int result = userService.activation(userId, code);
        if(result == ACTIVATION_SUCCESS){
            model.addAttribute("msg","激活成功!");
            model.addAttribute("target","/login");
        } else if(result == ACTIVATION_REPEAT){
            model.addAttribute("msg","无效操作，账号已激活!");
            model.addAttribute("target","/index");
        } else{
            model.addAttribute("msg","激活失败，激活码错误!");
            model.addAttribute("target","/index");
        }
        return "/site/operate-result";
    }

    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session){
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);
//        session.setAttribute("kaptcha",text);
        String kaptchaOwener = CommunityUtil.generateUUID();
        Cookie cookie = new Cookie("kaptchaOwener", kaptchaOwener);
        cookie.setMaxAge(60);
        cookie.setPath(CONTEXT_PATH);
        response.addCookie(cookie);
        String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwener);
        redisTemplate.opsForValue().set(redisKey, text, 60, TimeUnit.SECONDS);
        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image,"png", os);
        } catch (IOException e){
            logger.error("响应验证码失败" + e.getMessage());
        }
    }

    @RequestMapping(path = "/code", method = RequestMethod.POST)
    @ResponseBody
    public String getCode(String email, HttpServletResponse response){
        String code = kaptchaProducer.createText();
        //向邮箱发送验证码
        Map<String, Object> map = userService.sendCode(email, code);
        if(map.containsKey("emailMsg")){
            return CommunityUtil.getJsonString(405,"发送验证码失败,请检查邮箱是否正确");
        }
        String codeOwener = CommunityUtil.generateUUID();
        Cookie cookie = new Cookie("codeOwener", codeOwener);
        cookie.setMaxAge(300);
        cookie.setPath(CONTEXT_PATH);
        response.addCookie(cookie);
        String redisKey = RedisKeyUtil.getCodeKey(codeOwener);
        redisTemplate.opsForValue().set(redisKey, code, 300, TimeUnit.SECONDS);
        return CommunityUtil.getJsonString(200,"发送验证码成功");
    }

    @RequestMapping(path = "/resetPassword", method = RequestMethod.POST)
    public String resetPassword(String newPassword, String code, String email,
                                @CookieValue("codeOwener") String codeOwener,
                                Model model){
        String real_code = null;
        if(StringUtils.isNotBlank(codeOwener)){
            String redisKey = RedisKeyUtil.getCodeKey(codeOwener);
            real_code = (String) redisTemplate.opsForValue().get(redisKey);
        }
        if(StringUtils.isBlank(real_code) || StringUtils.isBlank(code) || !code.equalsIgnoreCase(real_code)){
            model.addAttribute("codeMsg","验证码不正确!");
            model.addAttribute("email", email);
            return "/site/forget";
        }
        Map<String, Object> map = userService.resetPassword(newPassword, email);
        if(map.containsKey("success")){
            return "redirect:/login";
        }else{
            model.addAttribute("emailMsg", map.get("emailMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("email", email);
            return "/site/forget";
        }

    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(String username, String password, String code, Boolean rememberme,
                        Model model, /*HttpSession session,*/ HttpServletResponse response,
                        @CookieValue("kaptchaOwener") String kaptchaOwener){
//        String kaptcha = (String) session.getAttribute("kaptcha");
        String kaptcha = null;
        if(StringUtils.isNotBlank(kaptchaOwener)){
            String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwener);
            kaptcha = (String) redisTemplate.opsForValue().get(redisKey);
        }
        if(StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)){
            model.addAttribute("codeMsg","验证码不正确!");
            return "/site/login";
        }
        int expiredSeconds = rememberme != null ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredSeconds);
        if(map.containsKey("ticket")){
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setPath(CONTEXT_PATH);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/index";
        }else{
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "/site/login";
        }
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        SecurityContextHolder.clearContext();
        return "redirect:/login";
    }
}
