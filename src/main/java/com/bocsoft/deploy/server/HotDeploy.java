package main.java.com.bocsoft.deploy.server;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import main.java.com.bocsoft.deploy.beans.Props;
import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import main.java.com.bocsoft.deploy.service.serviceIml.HotLoadServiceImp;
import java.util.Properties;

public class HotDeploy {
	private static final Logger logger = Logger.getLogger(HotDeploy.class);
	//配置文件信息
	private Properties props = new Props().getProps();


	private void startServer() {
		try {
				// Grab the Scheduler instance from the Factory
				Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

				// and start it off
				scheduler.start();

				// define the jobfvcrv and tie it to our HotLoadService class
				JobDetail job = newJob(HotLoadServiceImp.class)
						.withIdentity("job1_1", "jgroup1")
						.build();

				// Trigger the job to run now, and then repeat every 4 seconds
				Trigger trigger =  newTrigger()
						.withIdentity("trigger1_1", "tgroup1")
						.startNow()
						.withSchedule(simpleSchedule()
								.withIntervalInSeconds(Integer.parseInt(props.getProperty("internal")))
								.repeatForever())
								.build();

				// Tell quartz to schedule the job using our trigger
				scheduler.scheduleJob(job, trigger);
				//End this scheduler in 80s
				Thread.sleep(80000);

				scheduler.shutdown();

			}catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
	}



	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new HotDeploy().startServer();
	}

}
