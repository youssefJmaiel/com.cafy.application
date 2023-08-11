//package com.cafy.application.jwt;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new HandlerInterceptorAdapter() {
//            @Override
//            public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//                response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
//            }
//        });
//    }
//}
