package javaweb;

import java.io.IOException;
 
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
 
public class URLFilter implements Filter{
 
    @Override
    public void destroy() {
        System.out.println("destroy method of URLFilter is called");
    }
     
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
         
        HttpServletRequest request =  (HttpServletRequest) servletRequest;
        String url = request.getRequestURL().toString();
        System.out.println("url filter:" + url);
        filterChain.doFilter(servletRequest, servletResponse);
    }
 
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
         
    }
 
}