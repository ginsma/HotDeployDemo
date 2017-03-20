package main.java.com.bocsoft.deploy.service.serviceIml;


import main.java.com.bocsoft.deploy.service.HotLoadService;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.io.support.ResourcePatternResolver;


import test.java.com.bocsoft.deploy.beans.Car;
import test.java.com.bocsoft.deploy.beans.LoadInfo;
import test.java.com.bocsoft.deploy.beans.Users;
import test.java.com.bocsoft.deploy.dao.daoimpl.UsersDaoImpl;
import test.java.com.bocsoft.deploy.service.BaseManager;
import test.java.com.bocsoft.deploy.service.MyClassLoader;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by Jean on 2017/1/4.
 */
public class HotLoadServiceImp implements HotLoadService {
	//错误日志文件
	private Logger logger = Logger.getLogger("HotLoadServiceImp");
    //配置文件信息（全部）
	public static Properties props;

	//存放配置文件的时间戳(用于Spring)
	private static Map<String, Long> xmlTime = new HashMap<String, Long>();
	//全局的Spring的ApplicationContxt，每次配置文件更新都重新赋值(用于Spring)
	private static ApplicationContext ctxSpring;
	private String configType;

	//配置ibatis的Xml文件的相关参数（用于Ibatis）
	private Car car;


	//-----------加载Jar----------------
    // 类加载器
    private ClassLoader classLoader;

    //-----------加载Class----------------
    /**
     *记录热加载类的加载信息
     */
    private static final Map<String,LoadInfo> loadTimeMap = new HashMap<String, LoadInfo>();

    public static final String CLASS_PATH = "/Users/Jean/Work/HDSS/JavaHotDeploy/HotDeploy/out/production/HotDeploy/";
    //public static final String CLASS_PATH = "D:/EclipseWorkspace/HotDeploy/a/src/";

    public static final String MY_MANAGER = "test.java.com.bocsoft.deploy.service.serviceImpl.MyManager";


	//定时扫描配置文件的主程序
	public void execute(JobExecutionContext jobCtx) throws JobExecutionException {
		//加载config.properties
		Resource resource = new ClassPathResource(jobCtx.getJobDetail().getJobDataMap().getString("main/resources/config.properties"));

		//判断是否需要热加载Spring的配置文件
		if(isDeployed(resource,"hotdeploySpring")) {
			//配置Spring的Xml文件的地
			configType = "xml";
			try {
				loadFile(PropertiesLoaderUtils.loadProperties(resource).getProperty("filePathXml"), configType);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//配置Spring的Prop文件的地址
			configType = "prop";
			try {
				loadFile(PropertiesLoaderUtils.loadProperties(resource).getProperty("filePathProp"), configType);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

        //判断是否需要热加载Mybatis
		if(isDeployed(resource, "hotdeployIbatis")) {
			//配置Mybatis的Xml文件的地址
			loadMybatis();
		}

		//判断是否需要热加载jar文件
		if(isDeployed(resource,"hotdeployJar")) {
			//配置Mybatis的Xml文件的地址
			loadJar();
		}

		//判断是否需要热加载class文件
		if(isDeployed(resource,"hotdeployClass")) {
			//配置Mybatis的Xml文件的地址
			loadClass();
		}
	}

	//----------------------------公用的方法----------------------------------------
	private boolean isDeployed(Resource resource, String type) {
		int hotDeploy = 0;
		try {
			hotDeploy = Integer.parseInt(PropertiesLoaderUtils.loadProperties(resource).getProperty(type));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hotDeploy == 1;
	}
	//----------------------------加载Spring的方法----------------------------------------
	private void loadFile(String filePath ,String configType) {
		//使用系统文件路径方式加载文件
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

		try {
			Resource resources[] = resolver.getResources(filePath);
			for(Resource resource : resources) {
				loadSingleFile(configType, resource);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private void loadSingleFile(String configType, Resource resource)
			throws IOException {
		//配置文件上次更新的时间戳
		long lastFrame = resource.contentLength() + resource.lastModified();
		String resourceName =  resource.getFilename();
		//如果时间戳不存在
		if (!xmlTime.containsKey(resourceName)) {
			xmlTime.put(resourceName, lastFrame);
			System.out.println(resourceName + "的时间戳第一次初始化");
		} else if (!xmlTime.get(resourceName).equals(lastFrame)) {
			xmlTime.put(resourceName, lastFrame);
			System.out.println("------------------------------------------");
			System.out.println(resourceName + "的时间戳被更新");
			//更新配置文件
			if(configType.equals("xml") || configType == "xml")
				changeXml(resource);
			else if(configType.equals("prop") || configType == "prop")
				changeProp(resource);
		}
	}

	private void changeXml(Resource resource) throws IOException{
		String s = resource.getFile().getAbsolutePath();
		ctxSpring = new FileSystemXmlApplicationContext("file:" + s);
		car = (Car)ctxSpring.getBean("car3",Car.class);
		System.out.println(resource.getFilename()+ "的新车牌号为" + car.brand);

	}

	private void changeProp(Resource resource) {
		try {
			props = PropertiesLoaderUtils.loadProperties(resource);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(props.getProperty("boc"));
	}

	//----------------------------加载ibatis的方法----------------------------------------
	private void loadMybatis() {
		UsersDaoImpl studentDaoImpl = new UsersDaoImpl();

		System.out.println("测试查询所有");
		List<Users> users = studentDaoImpl.selectAllStudent();
		for (Users user : users) {
			System.out.println(user);
		}
	}

	//----------------------------加载Jar的方法----------------------------------------

	private void loadJar() {
		 String libpath = System.getProperty("user.dir") + File.separator + "lib";

         loadPath(libpath);

         System.out.println(loadClass("ognl.OgnlContext"));

         String jarPath = libpath + File.separator + "google-collections-1.0.jar";

         loadJar(jarPath);
	}



    /**
     * @Title loadPath
     * @Description 创建加载器
     * @Author
     * @param jarPath
     *            jar包所在路经
     * @throws
     */
    public void loadPath(String jarPath) {
        try {
            File jarFiles = new File(jarPath);

            File[] jarFilesArr = jarFiles.listFiles();
            URL[] jarFilePathArr = new URL[jarFilesArr.length];
            int i = 0;
            for (File jarfile : jarFilesArr) {
                String jarname = jarfile.getName();
                if (jarname.indexOf(".jar") < 0) {
                    continue;
                }
                //String jarFilePath = "file:\\" + jarPath + File.separator
                String jarFilePath = "file:\\" + jarPath + File.separator
                        + jarname;
                jarFilePathArr[i] = new URL(jarFilePath);
                i++;
            }

            classLoader = new URLClassLoader(jarFilePathArr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Title loadJar
     * @Description 遍历jar包下的类
     * @Author
     * @param jarName
     *            jar包名字 完整路径
     * @throws
     */
    public void loadJar(String jarName) {
        if (jarName.indexOf(".jar") < 0) {
            return;
        }
        try {
            JarFile jarFile = new JarFile(jarName);
            Enumeration<JarEntry> em = jarFile.entries();
            while (em.hasMoreElements()) {
                JarEntry jarEntry = em.nextElement();
                String clazzFile = jarEntry.getName();

                if (!clazzFile.endsWith(".class")) {
                    continue;
                }
                String clazzName = clazzFile.substring(0,
                        clazzFile.length() - ".class".length()).replace(
                        '/', '.');
                System.out.println(clazzName);

				loadClass(clazzName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Title loadClass
     * @Description 通过类加载器实例化
     * @Author
     * @param clazzName
     *            类名字
     * @return
     * @throws
     */
    public Object loadClass(String clazzName) {
        if (this.classLoader == null) {
            return null;
        }
        Class clazz = null;
        try {
            clazz = this.classLoader.loadClass(clazzName);
            Object obj = clazz.newInstance();
            return obj;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

  //----------------------------加载Class的方法----------------------------------------
    private void loadClass() {
    	 System.out.println("----------------------------");
    	 BaseManager manager = getManager(MY_MANAGER);
         manager.logic();

	}

    public static BaseManager getManager(String className){
        File loadFile = new File(CLASS_PATH+className.replaceAll("\\.","/")+".class");
        long lastModified = loadFile.lastModified();

        //查看是否被加载过 ,如果没有被加载过则加载
        if(loadTimeMap.get(className) == null){
            load(className,lastModified);
        }else if(loadTimeMap.get(className).getLoadTime() != lastModified){//如果被加载过，查看加载时间，如果该类已经被修改，则重新加载
            load(className,lastModified);
        }

        return loadTimeMap.get(className).getManager();
    }


    private static void load(String className,long lastModified){
        MyClassLoader myLoader = new MyClassLoader(CLASS_PATH);
        Class<?> loadClass = null;
        try {
            loadClass = myLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        BaseManager manager = newInstance(loadClass);
        LoadInfo loadInfo2 = new LoadInfo(myLoader,lastModified);
        loadInfo2.setManager(manager);
        loadTimeMap.put(className, loadInfo2);
    }


    private static BaseManager newInstance(Class<?> cls){
        try {
            return (BaseManager)cls.getConstructor(new Class[]{}).newInstance(new Object[]{});
        } catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }

}
