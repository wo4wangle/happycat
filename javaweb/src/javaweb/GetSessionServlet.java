package javaweb;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
 
public class GetSessionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("text/html;charset=UTF-8");
            String name_in_session = (String)request.getSession().getAttribute("name_in_session");
            response.getWriter().println(name_in_session);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
}