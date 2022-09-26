package com.hzwotu.operationlogsdk.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author chenl
 * @Date 2022/9/13 11:28 上午
 */
@Component
//extends OncePerRequestFilter
public class RequestReplaceFilter  implements Filter {
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
///*        if (!(request instanceof MyRequestWrapper)) {
//            request = new MyRequestWrapper(request);
//        }
//        filterChain.doFilter(request, response);*/
//
//
//        ServletRequest requestWrapper = null;
//        if(request instanceof HttpServletRequest) {
//            requestWrapper = new MyRequestWrapper((HttpServletRequest) request);
//        }
//        //获取请求中的流，将取出来的字符串，再次转换成流，然后把它放入到新 request对象中
//        // 在chain.doFiler方法中传递新的request对象
//        if(null == requestWrapper) {
//            filterChain.doFilter(request, response);
//        } else {
//            filterChain.doFilter(requestWrapper, response);
//        }
//    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        /*        if (!(request instanceof MyRequestWrapper)) {
            request = new MyRequestWrapper(request);
        }
        filterChain.doFilter(request, response);*/

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestURI = request.getRequestURI();
        if ("/auth/open_resource/file/upload".equals(requestURI) || "/auth/resource/file/upload".equals(requestURI) ){
            filterChain.doFilter(servletRequest, servletResponse);
        }else {
            ServletRequest requestWrapper = null;
            if(servletRequest instanceof HttpServletRequest) {
                requestWrapper = new MyRequestWrapper((HttpServletRequest) servletRequest);
            }
            //获取请求中的流，将取出来的字符串，再次转换成流，然后把它放入到新 request对象中
            // 在chain.doFiler方法中传递新的request对象
            if(null == requestWrapper) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                filterChain.doFilter(requestWrapper, servletResponse);
            }
        }

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
