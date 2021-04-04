package javaweb;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
  
public class ContextListener implements ServletContextListener {
  
    @Override
    public void contextDestroyed(ServletContextEvent e) {
        System.out.println("web app "+ e.getSource() +" is destroyed");
    }
  
    @Override
    public void contextInitialized(ServletContextEvent e) {
        System.out.println("web app "+ e.getSource() +" is initialized");
    }
}