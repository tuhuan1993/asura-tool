package com.asura.tools.timer;

import java.text.ParseException;
import java.util.Date;
import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;

import com.asura.tools.util.DateUtil;
import com.asura.tools.util.ExceptionUtil;

public class FlexTimer {
	public static Logger logger = Logger.getLogger(FlexTimer.class);
	private Hashtable<String, Scheduler> table;
	public static final String TASK = "task";
	private static FlexTimer ft;

	private FlexTimer() {
		this.table = new Hashtable();
	}

	public static synchronized FlexTimer getInstance() {
		if (ft == null) {
			ft = new FlexTimer();
		}

		return ft;
	}

	public boolean containsTask(String id) {
		return this.table.containsKey(id);
	}

	public void startTimer(String id, Object obj, String methodName, long interval) {
		startTimer(id, obj, methodName, interval, false);
	}

	public void startTimer(String id, Object obj, String methodName, int hour, int minute, boolean concurrent) {
		startTimer(id, obj, methodName, null, null, hour, minute, concurrent);
	}

	public void startTimer(String id, Object obj, String methodName, Class<?>[] classes, Object[] params, int hour,
			int minute) {
		startTimer(id, obj, methodName, classes, params, hour, minute, Boolean.FALSE.booleanValue());
	}

	public void startTimer(String id, Object obj, String methodName, Class<?>[] classes, Object[] params, int hour,
			int minute, boolean concurrent) {
		if (!(this.table.containsKey(id)))
			try {
				this.table.put(id, new StdSchedulerFactory().getScheduler());
				Task task = new Task();
				task.setTimerInterval(1000L);
				task.setId(id);
				ReflectionMethod rm = new ReflectionMethod();
				rm.setInstance(obj);
				rm.setMethodName(methodName);
				rm.setClassName(obj.getClass().getName());
				rm.setClasses(classes);
				rm.setParams(params);
				task.setMethod(rm);
				JobDetail jobDetail = null;
				if (concurrent)
					jobDetail = new JobDetail(id, "DEFAULT", StatefulTimerTask.class);
				else {
					jobDetail = new JobDetail(id, "DEFAULT", StatelessTimerTask.class);
				}
				JobDataMap jdm = new JobDataMap();
				jdm.put("task", task);
				jobDetail.setJobDataMap(jdm);
				Trigger trigger = TriggerUtils.makeDailyTrigger(id, hour, minute);

				((Scheduler) this.table.get(id)).scheduleJob(jobDetail, trigger);
				((Scheduler) this.table.get(id)).start();
				logger.info("timer '" + id + "' schedule task at " + DateUtil.getDateAndTimeString(new Date()));
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("error occured when schedule timer '" + id + "'. ");
			}
		else
			logger.info("timer '" + id + "' has already scheduled the task");
	}

	public void startTimer(String id, Object obj, String methodName, Class<?>[] classes, Object[] params, long interval,
			boolean concurrent) {
		SimpleTrigger trigger = new SimpleTrigger(id, null, -1, interval);
		startTimer(id, obj, methodName, classes, params, trigger, concurrent);
	}

	private void startTimer(String id, Object obj, String methodName, Class<?>[] classes, Object[] params,
			Trigger trigger, boolean concurrent) {
		if (!(this.table.containsKey(id)))
			try {
				this.table.put(id, new StdSchedulerFactory().getScheduler());
				Task task = new Task();
				task.setTimerInterval(1000L);
				task.setId(id);
				ReflectionMethod rm = new ReflectionMethod();
				rm.setInstance(obj);
				rm.setMethodName(methodName);
				rm.setClassName(obj.getClass().getName());
				rm.setClasses(classes);
				rm.setParams(params);
				task.setMethod(rm);
				JobDetail jobDetail = null;
				if (concurrent)
					jobDetail = new JobDetail(id, "DEFAULT", StatefulTimerTask.class);
				else {
					jobDetail = new JobDetail(id, "DEFAULT", StatelessTimerTask.class);
				}
				JobDataMap jdm = new JobDataMap();
				jdm.put("task", task);
				jobDetail.setJobDataMap(jdm);

				((Scheduler) this.table.get(id)).scheduleJob(jobDetail, trigger);
				((Scheduler) this.table.get(id)).start();
				logger.info("timer '" + id + "' schedule task at " + DateUtil.getDateAndTimeString(new Date()));
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("error occured when schedule timer '" + id + "'. ");
			}
		else
			logger.info("timer '" + id + "' has already scheduled the task");
	}

	public void startTimer(String id, Object obj, String methodName, String cron, boolean concurrent) {
		startTimer(id, obj, methodName, null, null, cron, concurrent);
	}

	public void startTimer(String id, Object obj, String methodName, Class<?>[] classes, Object[] params, String cron,
			boolean concurrent) {
		try {
			CronTrigger trigger = new CronTrigger(id, null, cron);
			startTimer(id, obj, methodName, classes, params, trigger, concurrent);
		} catch (ParseException e) {
			logger.info("cron parse failed.\n" + ExceptionUtil.getExceptionContent(e));
		}
	}

	public void startTimer(String id, Object obj, String methodName, long interval, boolean concurrent) {
		startTimer(id, obj, methodName, null, null, interval, concurrent);
	}

	public void startTimer(Task task) {
		if (!(this.table.containsKey(task.getId())))
			try {
				this.table.put(task.getId(), new StdSchedulerFactory().getScheduler());

				JobDetail jobDetail = new JobDetail(task.getId(), "DEFAULT", StatelessTimerTask.class);
				JobDataMap jdm = new JobDataMap();
				jdm.put("task", task);
				jobDetail.setJobDataMap(jdm);
				SimpleTrigger trigger = new SimpleTrigger(task.getId(), null, -1, task.getTimerInterval());

				((Scheduler) this.table.get(task.getId())).scheduleJob(jobDetail, trigger);
				((Scheduler) this.table.get(task.getId())).start();
				logger.info(
						"timer '" + task.getId() + "' schedule task at " + DateUtil.getDateAndTimeString(new Date()));
			} catch (SchedulerException e) {
				logger.info("error occured when schedule timer '" + task.getId() + "'. ");
			}
		else
			logger.info("timer '" + task.getId() + "' has already scheduled the task");
	}

	public void stopTimer(String id) {
		Scheduler timer = (Scheduler) this.table.get(id);
		if (timer != null) {
			try {
				timer.shutdown();
				this.table.remove(id);
			} catch (SchedulerException e) {
				logger.info("fail to stop timer '" + id + "'");
			}
			logger.info("timer '" + id + "' stoped at " + DateUtil.getDateAndTimeString(new Date()));
		} else {
			logger.info("timer '" + id + "' has not started yet");
		}
	}
}
