package com.asura.tools.monitor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FileLogger implements ILogger {
	private BufferedWriter writer;
	private FileOutputStream stream;

	public FileLogger(String fileName) throws MonitorException {
		try {
			this.stream = new FileOutputStream(new File(fileName));
			this.writer = new BufferedWriter(new OutputStreamWriter(this.stream));
		} catch (FileNotFoundException e) {
			throw new MonitorException(e);
		}
	}

	public void close() {
		try {
			this.writer.close();
			this.stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void debug(String message) {
		try {
			this.writer.append(message + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void error(String message) {
		try {
			this.writer.append(message + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void info(String message) {
		try {
			this.writer.append(message + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void warn(String message) {
		try {
			this.writer.append(message + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
