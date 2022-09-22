package com.hzwotu.operationlogsdk.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author chenl
 * @Date 2022/9/13 11:28 上午
 */
@Component
public class RequestReplaceFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!(request instanceof MyRequestWrapper)) {
            request = new MyRequestWrapper(request);
        }
        filterChain.doFilter(request, response);
    }
}
