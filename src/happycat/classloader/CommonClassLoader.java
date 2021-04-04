package happycat.classloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
//loader happycat special used classes
public class CommonClassLoader extends URLClassLoader {
 
    public CommonClassLoader() {
        super(new URL[] {});
 
        try {
            File workingFolder = new File(System.getProperty("user.dir"));
            File libFolder = new File(workingFolder, "lib");
            File[] jarFiles = libFolder.listFiles();
            for (File file : jarFiles) {
                if (file.getName().endsWith("jar")) {
                    URL url = new URL("file:" + file.getAbsolutePath());
                    this.addURL(url);
                }
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
 
    }
 
}