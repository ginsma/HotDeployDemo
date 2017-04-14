package main.java.com.bocsoft.deploy.service.serviceIml;


import java.net.URL;
import java.net.URLClassLoader;


/**
 * 插件类加载器，在插件目录中搜索jar包，并为发现的资源(jar)构造一个类加载器,将对应的jar添加到classpath中
 * @author
 */
public class MyURLClassLoader extends URLClassLoader {

    public MyURLClassLoader() {
        super(new URL[] {}, findParentClassLoader());
    }

    public void addURLFile(URL file) {
        addURL(file);
    }

    /**
     * 定位基于当前上下文的父类加载器
     * @return 返回可用的父类加载器.
     */
    private static ClassLoader findParentClassLoader() {
        ClassLoader parent = HotLoadServiceImp.class.getClassLoader();
        if (parent == null) {
            parent = MyURLClassLoader.class.getClassLoader();
        }
        if (parent == null) {
            parent = ClassLoader.getSystemClassLoader();
        }
        return parent;
    }
}