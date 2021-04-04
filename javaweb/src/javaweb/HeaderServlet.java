package javaweb;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
public class HeaderServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response){
        try {
            String userAgent = request.getHeader("User-Agent");
            response.getWriter().println(userAgent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
}