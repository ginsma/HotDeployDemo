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
import test.java.com.bocsoft.deploy.beans.Users;
import test.java.com.bocsoft.deploy.dao.daoimpl.UsersDaoImpl;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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

}
