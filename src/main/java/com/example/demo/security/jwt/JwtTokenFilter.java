package com.example.demo.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenFilter extends GenericFilterBean {
private static final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);

@Autowired
private JwtProvider jwtProvider;

    public JwtTokenFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        try {
//            String token = getJwt(request);
//            if(token !=null &&JwtProvider.validateToken(token)){
//                String username = JwtProvider.getUerNameFromToken(token);
////                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//                UsernamePasswordAuthenticationToken authenticationToken = jwtProvider.getAuthentication(token);
//                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//            }
//        } catch (Exception e){
//            logger.error("Can't set user authentication -> Message: {}", e.getMessage());
//        }
//        filterChain.doFilter(request,response);
//    }
    public static String getJwt(ServletRequest request){
        String authHeader = ((HttpServletRequest) request).getHeader("Authorization");
        if(authHeader !=null && authHeader.startsWith("Bearer")){
            return authHeader.replace("Bearer", "");
        }
        return null;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        try {
            String token = getJwt(request);
            if(token !=null &&JwtProvider.validateToken(token)){
                String username = JwtProvider.getUerNameFromToken(token);
//                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken = jwtProvider.getAuthentication(token);
//                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e){
            logger.error("Can't set user authentication -> Message: {}", e.getMessage());
        }
        chain.doFilter(request,response);
    }
}
