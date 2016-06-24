package com.asura.tools.monitor;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.SerializableEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.asura.tools.timer.IntervalExecutor;
import com.asura.tools.util.ExceptionUtil;

public class MonitorService {
	private IntervalExecutor executor;
	private MonitorDefinition definition;

	public MonitorService(InputStream stream) throws ConfigurationParseException {
		this.definition = MonitorXMLConverter.getMonitorDefinition(stream);
		this.executor = getIntervalExecutor();
		this.executor.setName("RegiterMonitor");
		this.executor.setInterval(this.definition.getRegisterInterval());
		FileHierarchy.setPath(this.definition.getLocation());
	}

	public void start() {
		this.executor.execute();
	}

	public MonitorDefinition getDefinition() {
		return this.definition;
	}

	public void setDefinition(MonitorDefinition definition) {
		this.definition = definition;
	}

	private IntervalExecutor getIntervalExecutor() {
		return new IntervalExecutor() {
			public void execute() {
				for (Category cate : MonitorService.this.definition.getCategories())
					try {
						HttpEntity entity = new SerializableEntity(cate, false);
						HttpPost post = new HttpPost(MonitorService.this.definition.getMonitorUrl());
						post.setEntity(entity);
						HttpClient httpclient = new DefaultHttpClient();
						httpclient.execute(post);
					} catch (Exception e) {
						throw new MonitorRegisterException("事件'" + cate.getResult().getKey() + "'不能完成注册, 注册url为'"
								+ MonitorService.this.definition.getMonitorUrl() + "'.\n"
								+ ExceptionUtil.getExceptionContent(e));
					}
			}
		};
	}
}
