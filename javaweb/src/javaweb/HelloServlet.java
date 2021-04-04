package javaweb;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class HelloServlet extends HttpServlet {
    public HelloServlet(){
        System.out.println(this + " construction method");
    }

    public void init(ServletConfig config){
        String author = config.getInitParameter("author");
        String site = config.getInitParameter("site");

        System.out.println(this + " init method:");
        System.out.println("para author:" + author);
        System.out.println("para site:" + site);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response){
        try {
            System.out.println(this + " doGet() method");
            response.getWriter().println("Hello happycat from HelloServlet@javaweb " + this);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void destroy(){
        System.out.println(this + " destroyed!");
    }

}