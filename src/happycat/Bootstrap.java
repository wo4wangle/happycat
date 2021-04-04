package happycat;

import happycat.classloader.CommonClassLoader;

import java.lang.reflect.Method;

//start happycat
public class Bootstrap {
    public static void main(String[] args) throws Exception {
        // start happycat
        // 1. new CommonClassLoader for loading Server class
        // 2. set current ClassLoader to be CommonClassLoader
        // 3. load Server Class with CommonClassLoader
        // 4. invoke Server start method with reflex
        CommonClassLoader commonClassLoader = new CommonClassLoader();

        Thread.currentThread().setContextClassLoader(commonClassLoader);

        String serverClassName = "happycat.catalina.Server";

        Class<?> serverClazz = commonClassLoader.loadClass(serverClassName);

        Object serverObject = serverClazz.newInstance();

        Method m = serverClazz.getMethod("start");

        m.invoke(serverObject);

    }
}