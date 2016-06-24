package com.asura.tools.util.performance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import com.asura.tools.util.StringUtil;

public class UrlSet {
	private HashSet<String> urls;

	public UrlSet() {
		this.urls = new LinkedHashSet();
	}

	public void addUrl(String url) {
		this.urls.add(url);
	}

	public Iterator<String> getUrls() {
		return this.urls.iterator();
	}

	public static List<String> urlListFromFile(String fileName) {
		List list = new ArrayList();
		try {
			FileInputStream stream = new FileInputStream(new File(fileName));
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "utf8"));

			while (reader.ready()) {
				String line = reader.readLine();
				if ((!(StringUtil.isNullOrEmpty(line))) && (line.startsWith("http"))) {
					list.add(line);
				}
			}

			reader.close();
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public static Iterator<String> UrlSetFromFile(final String fileName) {
		return new Iterator() {
			private BufferedReader reader;
			private FileInputStream stream;

			{
				try {
					stream = new FileInputStream(new File(fileName));
					reader = new BufferedReader(new InputStreamReader(stream, "utf8"));
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			public boolean hasNext() {
				boolean hasNext = false;
				try {
					hasNext = this.reader.ready();
					if (!(hasNext)) {
						this.reader.close();
						this.stream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				return hasNext;
			}

			public String next() {
				try {
					String line = this.reader.readLine();
					if (StringUtil.isNullOrEmpty(line)) {
						line = "";
					}

					return line;
				} catch (IOException e) {
					e.printStackTrace();
				}

				return "";
			}

			public void remove() {
			}
		};
	}
}
