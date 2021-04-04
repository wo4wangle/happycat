package happycat.catalina;

import cn.hutool.core.util.ArrayUtil;

import javax.servlet.*;
import java.io.IOException;
import java.util.List;
// do filter-chain for a servlet
// build object with filterList and servlet
// do all filter in filterList then proceeding servlet service
public class ApplicationFilterChain implements FilterChain{
     
    private Filter[] filters;
    private Servlet servlet;
    int pos;
     
    public ApplicationFilterChain(List<Filter> filterList,Servlet servlet){
        this.filters = ArrayUtil.toArray(filterList,Filter.class);
        this.servlet = servlet;
    }
 
    @Override
    public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        if(pos < filters.length) {
             Filter filter= filters[pos++];
             filter.doFilter(request, response, this);
        }
        else {
            servlet.service(request, response);
        }
    }
 
}