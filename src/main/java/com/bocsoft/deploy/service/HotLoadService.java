package main.java.com.bocsoft.deploy.service;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by Jean on 2017/1/4.
 */
public interface HotLoadService extends Job {
   
    public void execute(JobExecutionContext jobCtx) throws JobExecutionException;
       
}
