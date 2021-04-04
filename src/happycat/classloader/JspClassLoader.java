package happycat.classloader;

import cn.hutool.core.util.StrUtil;
import happycat.catalina.Context;
import happycat.util.Constant;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
 //loader JSP class
// build a uri-jspClassloader map, if a class is updated, update the uri-jspClassLoader to a new jspClassLoader
public class JspClassLoader extends URLClassLoader {
 
    private static Map<String, JspClassLoader> map = new HashMap<>();
 
    public static void invalidJspClassLoader(String uri, Context context) {
        // remove key form map is invaliding a jspClassLoader
        String key = context.getPath() + "/" + uri;
        map.remove(key);
    }
 
    public static synchronized JspClassLoader getJspClassLoader(String uri, Context context) {
        // get jspClassLoader from map with uri
        // if not exist, then build it
        String key = context.getPath() + "/" + uri;
        JspClassLoader loader = map.get(key);
        if (null == loader) {
            loader = new JspClassLoader(context);
            map.put(key, loader);
        }
        return loader;
    }
 
    private JspClassLoader(Context context) {
        //build a jspClassLoader with context
            //just add scanning path is enough to loader target class and the path is just in context
        super(new URL[] {}, context.getWebClassLoader());
 
        try {
            String subFolder;
            String path = context.getPath();
            if ("/".equals(path))
                subFolder = "_";
            else
                subFolder = StrUtil.subAfter(path, '/', false);
 
            File classesFolder = new File(Constant.workFolder, subFolder);
            URL url = new URL("file:" + classesFolder.getAbsolutePath() + "/");
            this.addURL(url);
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
}