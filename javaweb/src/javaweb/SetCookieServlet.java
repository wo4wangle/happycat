package javaweb;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
public class SetCookieServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response){
        try {
            Cookie c = new Cookie("name", "LeWang(cookie)");
            c.setMaxAge(60 * 24 * 60);
            c.setPath("/");
            response.addCookie(c);
            response.getWriter().println("set cookie successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
}