package main.java.com.bocsoft.deploy.server;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import main.java.com.bocsoft.deploy.service.serviceIml.HotLoadServiceImp;

public class HotDeploy {
	//配置文件地址
	private static final String CONFIG_PATH = "main/resources/config.properties";
	private static final String APPLICATION_PATH = "main/resources/applicationContext.xml";

	public void startServer() {
		
			try {
				// Grab the Scheduler instance from the Factory
				Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

				// and start it off
				scheduler.start();

				// define the jobfvcrv and tie it to our HotLoadService class
				JobDetail job = newJob(HotLoadServiceImp.class)
						.withIdentity("job1_1", "jgroup1")
						.build();
				
				//initialization parameters
				job.getJobDataMap().put("main/resources/config.properties", CONFIG_PATH);
				job.getJobDataMap().put("main/resources/applicationContext.xml", APPLICATION_PATH);

				// Trigger the job to run now, and then repeat every 4 seconds
				Trigger trigger =  newTrigger()
						.withIdentity("trigger1_1", "tgroup1")
						.startNow()
						.withSchedule(simpleSchedule()
								.withIntervalInSeconds(4)
								.repeatForever())
								.build();

				// Tell quartz to schedule the job using our trigger
				scheduler.scheduleJob(job, trigger);
				//End this schedJarTest1.jaruler in 80s
				Thread.sleep(80000);

				scheduler.shutdown();

			}catch (Exception e) {
				e.printStackTrace();
			}
	}



	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new HotDeploy().startServer();
	}

}
