package com.asura.tools.word;

import java.util.Hashtable;

public class SourceManager {
	private Hashtable<String, InformationSource> table;
	private static SourceManager instance;

	private SourceManager() {
		this.table = new Hashtable();
	}

	public static synchronized SourceManager getInstance() {
		if (instance == null) {
			instance = new SourceManager();
		}

		return instance;
	}

	public synchronized InformationSource getSource(String source) {
		if (!(this.table.containsKey(source))) {
			this.table.put(source, new InformationSource(source));
		}

		return ((InformationSource) this.table.get(source));
	}
}
