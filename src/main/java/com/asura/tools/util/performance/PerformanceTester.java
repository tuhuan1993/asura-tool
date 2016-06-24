package com.asura.tools.util.performance;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.asura.tools.util.StringUtil;

public class PerformanceTester {
	private PerformanceInfo info;
	private PerformanceResult result;

	public static void main(String[] args) throws InterruptedException {
		PerformanceTester tester = new PerformanceTester();
		if ((args == null) || (args.length == 0)) {
			args = new String[1];
			args[0] = PerformanceInfo.class.getClassLoader().getResource("performance.xml").getPath();
		}
		tester.setInfo(PerformanceInfo.fromFile(args[0]));
		tester.run();

		Thread.sleep(1000000000L);
	}

	public void run() {
		List list = UrlSet.urlListFromFile(this.info.getPath());
		for (int i = 0; i < this.info.getThreadCount(); ++i) {
			Thread th = new Thread(getRunnable(i, this.info.getExeTime(), list));
			th.start();
		}
	}

	private Runnable getRunnable(int i, final int count, final List<String> list) {
		return new Runnable() {
			public void run() {
				HttpClient httpclient = new DefaultHttpClient();
				synchronized (PerformanceTester.this.info) {
					if (PerformanceTester.this.result == null) {
						PerformanceTester.this.result = new PerformanceResult(
								(Long[]) PerformanceTester.this.info.getTimes().toArray(new Long[0]),
								PerformanceTester.this.info.getPrintCount(), PerformanceTester.this.info);
						if (PerformanceTester.this.info.getCollector() != null) {
							PerformanceTester.this.info.getCollector().getResult()
									.setPrintCount(PerformanceTester.this.result.getPrintCount());
							PerformanceTester.this.info.getCollector().getResult()
									.setTimes(PerformanceTester.this.result.getTimes());
						}
					}
				}
				for (int i = 0; i < count; ++i)
					runTest(httpclient);
			}

			private void runTest(HttpClient httpclient) {
				for (int i = 0; i <list.size(); ++i) {
					String url = (String) list
							.get(Double.valueOf(Math.random() * list.size()).intValue());
					if (!(StringUtil.isNullOrEmpty(url))) {
						HttpGet post = new HttpGet(url);
						long s = System.currentTimeMillis();
						try {
							HttpResponse respose = httpclient.execute(post);
							long e = System.currentTimeMillis();

							byte[] bs = EntityUtils.toByteArray(respose.getEntity());

							String charset = EntityUtils.getContentCharSet(respose.getEntity());
							if (StringUtil.isNullOrEmpty(charset)) {
								charset = "utf-8";
							}
							String content = new String(bs, charset);

							if (PerformanceTester.this.info.getCollector() != null) {
								PerformanceTester.this.info.getCollector().getResult().record(url,
										PerformanceTester.this.info.getCollector().getTime(content),
										respose.getStatusLine().getStatusCode(), bs.length, content);

								PerformanceTester.this.info.getCollector().getResult().getCount();
							}
							PerformanceTester.this.result.record(url, e - s, respose.getStatusLine().getStatusCode(),
									bs.length, content);

							PerformanceTester.this.result.getCount();
						} catch (Exception e) {
							long end = System.currentTimeMillis();
							PerformanceTester.this.result.record(url, end - s, -50, 0L, "");
							e.printStackTrace();
						}
					}
				}
			}
		};
	}

	private long getLong(String time) {
		time = time.replace("耗时：", "");

		return Long.valueOf(time).longValue();
	}

	public PerformanceInfo getInfo() {
		return this.info;
	}

	public void setInfo(PerformanceInfo info) {
		this.info = info;
	}
}
