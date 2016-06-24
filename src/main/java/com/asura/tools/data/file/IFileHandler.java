package com.asura.tools.data.file;

import java.io.File;

public interface IFileHandler {
	public void successFile(File paramFile);

	public void failFile(File paramFile);

	public void failFile(File paramFile, Exception paramException);
}
