package main.java.com.bocsoft.deploy.service.serviceIml;


import main.java.com.bocsoft.deploy.beans.Props;
import main.java.com.bocsoft.deploy.service.HotLoadService;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.io.support.ResourcePatternResolver;


import test.java.com.bocsoft.deploy.beans.Car;
import test.java.com.bocsoft.deploy.beans.Users;
import test.java.com.bocsoft.deploy.dao.daoimpl.UsersDaoImpl;
import test.java.com.bocsoft.deploy.service.JarTest1;
import test.java.com.bocsoft.deploy.service.Say;


import java.io.IOException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by Jean on 2017/1/4.
 */
public class HotLoadServiceImp implements HotLoadService {
	//错误日志文件
	private static final Logger logger = Logger.getLogger(HotLoadServiceImp.class);
    //配置文件信息（全部）
	private Properties props = new Props().getProps();
	private static boolean isFirstLoad = true;
	private static final String LOADSPRING = "loadSpring";
	private static final String LOADIBATIS = "loadIbatis";
	private static final String LOADCLASS = "loadClass";
	private static final String LOADJAR = "loadJar";
	private static final String XMLPATH = "xmlPath";
	private static final String PROPPATH = "propPath";
	private static final String CLASSPATH = "classPath";
	private static final String JARPATH = "jarPath";
	private static final String XML = "xml";
	private static final String PROP = "prop";
	private static final String CLASS = "class";
	private static final String JAR = "jar";

	//存放配置文件的时间戳(用于Spring)
	private static Map<String, Long> xmlTime = new HashMap<String, Long>();
	//全局的Spring的ApplicationContxt，每次配置文件更新都重新赋值(用于Spring)
	private ApplicationContext ctxSpring;

	//配置ibatis的Xml文件的相关参数（用于Ibatis）
	private Car car;

    //配置class文件的相关参数（用于Class）
    private Say say;

	//配置jar文件的相关参数（用于Jar）
	private MyURLClassLoader URLClassLoader;
    private static final  String CLAZZ_SUFFIX = ".class";
    private static List<String> className = new ArrayList<String>();

    //定时扫描配置文件的主程序
	public void execute(JobExecutionContext jobCtx) throws JobExecutionException {
		//判断是否需要热加载Spring的配置文件
		if(isDeployed(LOADSPRING)) {
			loadFile(props.getProperty(XMLPATH), XML);
			loadFile(props.getProperty(PROPPATH), PROP);
		}

        //判断是否需要热加载Mybatis
		if(isDeployed(LOADIBATIS)) {
			loadIbatis();
		}

        //判断是否需要热加载Class
        if(isDeployed(LOADCLASS)) {
			loadFile(props.getProperty(CLASSPATH), CLASS);
        }

		//判断是否需要热加载Jar
		if(isDeployed(LOADJAR)) {
			loadFile(props.getProperty(JARPATH), JAR);
		}
		isFirstLoad = false;
	}

	//----------------------------公用的方法----------------------------------------
	private boolean isDeployed(String type) {
		int hotDeploy = 0;
		hotDeploy = Integer.parseInt(props.getProperty(type));
		return hotDeploy == 1;
	}
	//----------------------------加载Spring、class和jar的共用方法----------------------------------------
	private void loadFile(String filePath, String configType) {
		//使用系统文件路径方式加载文件
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		try {
			Resource resources[] = resolver.getResources(filePath);
			for(Resource resource : resources) {
				loadSingleFile(configType, resource);
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

	}


	private void  loadSingleFile(String configType, Resource resource) {
		//配置文件上次更新的时间戳
		long lastFrame = 0;
		try {
			lastFrame = resource.contentLength() + resource.lastModified();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		String resourceName =  resource.getFilename();
		//如果时间戳不存在
		if ((!xmlTime.containsKey(resourceName)) && isFirstLoad) {
			xmlTime.put(resourceName, lastFrame);
			logger.info(resourceName + "的时间戳第一次初始化");
		} else if ((!xmlTime.containsKey(resourceName)) || (!xmlTime.get(resourceName).equals(lastFrame))) {
			xmlTime.put(resourceName, lastFrame);
			logger.info("------------------------------------------");
			logger.info(resourceName + "的时间戳被更新");
			//更新配置文件
			if(configType.equals(XML) || configType == XML)
                changeXml(resource);
			else if(configType.equals(PROP) || configType == PROP)
				changeProp(resource);
			else if(configType.equals(CLASS) || configType == CLASS)
                changeClass(resource);
			else if(configType.equals(JAR) || configType == JAR)
				changeJar(resource);
		}
	}

	//----------------------------加载Spring的方法---------------------------------------

	private void changeXml(Resource resource) {
		try {
			ctxSpring = new FileSystemXmlApplicationContext("file:" + resource.getFile().getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		car = (Car)ctxSpring.getBean("car3",Car.class);
		logger.info(resource.getFilename()+ "的新车牌号为" + car.brand);

	}

	private void changeProp(Resource resource) {
		Properties property = null;
		try {
			property = PropertiesLoaderUtils.loadProperties(resource);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		logger.info(property.getProperty("boc"));
	}

	//----------------------------加载ibatis的方法---------------------------------------
    /*原理是用重写SqlmapClientFactoryBean让它返回我们重写过的SqlMapClientImpl，
      在SqlMapClientImpl添加刷新的方法，然后通过SqlMapClientImpl来调用
      我们代理的SqlMapExecutorDelegate就可以实现重新加载了
      SqlMapExecutorDelegate是具体的加载类，里面有一个判别 */
	private void loadIbatis() {
		UsersDaoImpl studentDaoImpl = new UsersDaoImpl();

		logger.info("测试查询所有");
		List<Users> users = studentDaoImpl.selectAllStudent();
		for (Users user : users) {
			logger.info(user);
		}
	}

    //----------------------------加载Class的方法----------------------------------------
    //原来是根据跳过ClassLoader的loadClass方法，而是直接执行defineClass方法或者是findClass来加载Class文件
    private void changeClass(Resource resource) {
		Object ClassTemp = null;
		//直接执行自己ClassLoader的findNewClass方法来调用defineClass方法，跳过验证ClassLoader的步骤
		try {
			ClassTemp = MyClassLoader.GetInstance().findNewClass(resource.getFile().toString());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		say = MyClassLoader.reLoadClass(ClassTemp);
        say.say();
		logger.info(say.getClass().getName() + "成功被热加载 ");
    }

	//----------------------------加载jar的方法----------------------------------------
    //原理是根据不同的ClassLoader热加载同一个Class不会被判定为相同的Class文件
	private void changeJar(Resource resource) {
	    URLClassLoader = new MyURLClassLoader();
        //把Jar包的路径放入URLClassLoader中
		try {
			URLClassLoader.addURLFile(resource.getURL());
            loadClassName(String.valueOf(resource.getFile()));
        } catch (IOException e) {
			logger.error(e.getMessage(), e);
        }

        Iterator it = className.iterator();
        //加载Class文件
        while(it.hasNext()) {
            Class<?> forName = null;
            try {
                forName = Class.forName(it.next() + "", true, URLClassLoader);
                URLClassLoader.loadClass(it.next() + "");
            } catch (ClassNotFoundException e) {
				logger.error(e.getMessage(), e);
            }

			logger.info("load " + resource.getFilename() + "  success");

            JarTest1 jarTest = null;
            try {
                jarTest = (JarTest1) forName.newInstance();
            } catch (InstantiationException e) {
				logger.error(e.getMessage(), e);
            } catch (IllegalAccessException e) {
				logger.error(e.getMessage(), e);
            }
            jarTest.f();
        }
        className.clear();
	}

    private void loadClassName(String jarName) {
        try {
            JarFile jarFile = new JarFile(jarName);
            Enumeration<JarEntry> em = jarFile.entries();
            while (em.hasMoreElements()) {
                JarEntry jarEntry = em.nextElement();
                String clazzFile = jarEntry.getName();

                if (!clazzFile.endsWith(CLAZZ_SUFFIX)) {
                    continue;
                }
                String clazzName = clazzFile.substring(0,
                        clazzFile.length() - CLAZZ_SUFFIX.length()).replace(
                        '/', '.');
                className.add(clazzName);
            }
        } catch (IOException e) {
			logger.error(e.getMessage(), e);
        }
    }
}
