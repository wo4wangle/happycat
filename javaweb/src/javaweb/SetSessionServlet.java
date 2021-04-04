package javaweb;

import java.io.IOException;
 
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
public class SetSessionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
 
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("text/html;charset=UTF-8");
            request.getSession().setAttribute("name_in_session", "LeWang(session)");
            response.getWriter().println(request.getSession().getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
}