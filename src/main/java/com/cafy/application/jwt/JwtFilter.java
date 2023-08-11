package com.cafy.application.jwt;

import io.jsonwebtoken.Claims;
import javax.servlet.FilterChain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.ServletException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    CustomerUserDetailsService service;

    Claims claims = null;
    private String userName= null;



    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {



        if (httpServletRequest.getMethod().equals("OPTIONS"))
        {
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }



        if (httpServletRequest.getServletPath().matches("/user/login|/user/forgotPassword|/user/signup")){
            filterChain.doFilter(httpServletRequest,httpServletResponse);
        }else {

            String authorizationHeader = httpServletRequest.getHeader("Authorization");
            String token = null;

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
                token = authorizationHeader.substring(7);
                userName = jwtUtils.extractUsername(token);
                claims = jwtUtils.extractAllClaims(token);

            }

            if (userName != null && SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails = service.loadUserByUsername(userName);
                if (jwtUtils.validateToken(token,userDetails)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails( httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }

            filterChain.doFilter(httpServletRequest,httpServletResponse);
        }
    }

    public boolean isAdmin(){
        return "admin".equalsIgnoreCase((String) claims.get("role"));
    }
    public boolean isUser(){
        return "user".equalsIgnoreCase((String) claims.get("role"));
    }
    public String getCurrentUser(){
        return userName;
    }


}
