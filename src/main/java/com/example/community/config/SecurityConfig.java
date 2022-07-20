package com.example.community.config;

import com.example.community.util.CommunityConstant;
import com.example.community.util.CommunityUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import java.io.PrintWriter;

/**
 * @Author INSLYAB
 * @Date 2022/7/19 12:21
 */
@Configuration
public class SecurityConfig implements CommunityConstant {

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().antMatchers("/resources/**");
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //授权
        http.authorizeRequests()
                .antMatchers(
                 "/user/setting",
                 "/user/upload",
                 "/discuss/add",
                 "/comment/add/**",
                 "/letter/**",
                 "/notice/**",
                 "/like",
                 "/follow",
                 "/unfollow"
                )
                .hasAnyAuthority(
                    AUTHORITY_USER,
                    AUTHORITY_ADMIN,
                    AUTHORITY_MODERATOR
                )
                .antMatchers(
                        "/discuss/top",
                        "/discuss/wonderful"
                )
                .hasAnyAuthority(
                        AUTHORITY_MODERATOR
                )
                .antMatchers(
                        "/discuss/delete",
                        "/data/**"
                )
                .hasAnyAuthority(
                        AUTHORITY_ADMIN
                )
                .anyRequest().permitAll()
                .and().csrf().disable();

        //未登录、权限不够时处理
        http.exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    //没有登陆时处理
                    String xRequestedWith = request.getHeader("x-requested-with");
                    if("XMLHttpRequest".equals(xRequestedWith)){
                        //异步请求
                        response.setContentType("application/plain;charset=utf-8");
                        PrintWriter writer = response.getWriter();
                        writer.write(CommunityUtil.getJsonString(403, "未登录"));
                    }else{
                        //普通请求
                        response.sendRedirect(request.getContextPath() + "/login");
                    }
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    //权限不足时处理
                    String xRequestedWith = request.getHeader("x-requested-with");
                    if("XMLHttpRequest".equals(xRequestedWith)){
                        //异步请求
                        response.setContentType("application/plain;charset=utf-8");
                        PrintWriter writer = response.getWriter();
                        writer.write(CommunityUtil.getJsonString(403, "权限不足"));
                    }else{
                        //普通请求
                        response.sendRedirect(request.getContextPath() + "/denied");
                    }
                });

        // 覆盖默认的logout 跳过该方法
        http.logout().logoutUrl("/security/logout");
        return http.build();
    }

}
