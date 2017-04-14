package main.java.com.bocsoft.deploy.service.serviceIml;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Jean on 2017/3/30.
 */
public class MyClassLoader extends ClassLoader {
    private static final Logger logger = Logger.getLogger(HotLoadServiceImp.class);

    //构造器私有化,禁止使用者直接生成实例
    private MyClassLoader() {}

    //获取MyClassLoads的唯一方法
    public static MyClassLoader GetInstance() {
        return new MyClassLoader();
    }

    /**
     * @param classPath
     * @return Object
     * 热加载新的Class类
     */
    public Object findNewClass(String classPath) {
        try {
            byte[] b = getBytes(classPath);
            return defineClass(null, b, 0, b.length).newInstance();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

/*    public Class<?> loadClass(String name,boolean resolve) throws ClassNotFoundException {
        return findClass(name);
    }*/

    /**
     * @param o
     * @return T
     * 包装返回类对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T reLoadClass(Object o) {
        return (T) o;
    }

    /**
     * @param filename
     * @return Byte[]
     * @throws IOException
     *  返回class文件的Byte
     */
    private byte[] getBytes(String filename) throws IOException {
        File file = new File(filename);
        byte raw[] = new byte[(int) file.length()];
        FileInputStream fin = new FileInputStream(file);
        fin.read(raw);
        fin.close();
        return raw;
    }
}
