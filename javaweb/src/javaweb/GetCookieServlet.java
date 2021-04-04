package javaweb;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
public class GetCookieServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response){
        try {
            Cookie[] cookies = request.getCookies();
            if (null != cookies){
                for (int d = 0; d <= cookies.length - 1; d++) {
                    response.getWriter().print(cookies[d].getName() + ":" + cookies[d].getValue() + "<br>");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
}