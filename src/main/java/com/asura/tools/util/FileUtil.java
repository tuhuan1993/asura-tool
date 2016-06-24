package com.asura.tools.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.asura.tools.data.DataIterator;
import com.asura.tools.data.EmptyDataIterator;

public class FileUtil {
	public static String[] getContentByLine(String fileName) {
		return getContentByLine(fileName, null);
	}

	public static String[] getContentByLine(String fileName, String encoding) {
		try {
			return getContentByLine(new FileInputStream(fileName), encoding);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return new String[0];
	}

	public static void output(String file, byte[] bs) {
		try {
			FileUtils.writeByteArrayToFile(new File(file), bs);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static byte[] getBytes(String file) {
		try {
			return FileUtils.readFileToByteArray(new File(file));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new byte[0];
	}

	public static String getUrlFileName(String url) {
		try {
			URL u = new URL(url);
			return getChar(u.getHost()) + "/" + getChar(u.getFile());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return url;
	}

	private static String getChar(String s) {
		char[] cs = s.toCharArray();
		List list = new ArrayList();
		for (char c : cs) {
			if ((StringUtil.isEnglishOrNumberCharacter(c)) || (c == '.') || (c == '&') || (c == '=') || (c == '/')
					|| (c == '%'))
				list.add(String.valueOf(c));
			else if (c == '?') {
				list.add("？");
			}
		}

		return StringUtil.getStringFromStrings(list, "");
	}

	public static DataIterator<String> getContent(String fileName, String encoding) {
		try {
			return getContent(new FileInputStream(fileName), encoding);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return new EmptyDataIterator();
	}

	public static DataIterator<String> getContent(final FileInputStream stream, final String encoding) {
		return new DataIterator<String>() {
			private BufferedReader bfReader;

			{
				try {
					if (encoding != null)
						bfReader = new BufferedReader(new InputStreamReader(stream, encoding));
					else
						bfReader = new BufferedReader(new InputStreamReader(stream));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

			}
			@Override
			public boolean hasNext() {
				if (this.bfReader != null) {
					try {
						boolean has = this.bfReader.ready();
						if (!(has)) {
							close();
						}
						return has;
					} catch (IOException e) {
						return false;
					}
				}
				return false;
			}

			@Override
			public String next() {
				try {
					return this.bfReader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return "";
			}

			@Override
			public void close() {
				try {
					stream.close();
					this.bfReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}

			@Override
			public void reset() {
				// TODO Auto-generated method stub
				
			}
		};
	}

	public static String[] getContentByLine(InputStream stream, String encoding) {
		ArrayList list = new ArrayList();
		try {
			BufferedReader bfReader = null;
			if (encoding != null)
				bfReader = new BufferedReader(new InputStreamReader(stream, encoding));
			else {
				bfReader = new BufferedReader(new InputStreamReader(stream));
			}

			while (bfReader.ready()) {
				list.add(bfReader.readLine());
			}
			stream.close();
			bfReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ((String[]) list.toArray(new String[0]));
	}

	public static void moveFiles(String sourceDic, String targetDic) {
		String[] files = new File(sourceDic).list();
		for (String file : files) {
			File oldFile = new File(sourceDic + "//" + file);
			File newFile = new File(targetDic + "//" + file);
			if (oldFile.exists())
				oldFile.renameTo(newFile);
		}
	}

	public static void deleteFile(String path) {
		File file = new File(path);
		if (file.exists())
			file.delete();
	}

	public static void createFolder(String path) {
		File file = new File(path);
		if (!(file.exists()))
			file.mkdirs();
	}

	public static void copyFile(String sourcePath, String targetPath) throws IOException {
		FileUtils.copyFile(new File(sourcePath), new File(targetPath));
	}

	public static boolean clearFolder(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!(file.exists())) {
			return flag;
		}
		if (!(file.isDirectory())) {
			return flag;
		}
		String[] tempList = file.list();
		if ((tempList == null) || (tempList.length <= 0)) {
			return true;
		}
		File temp = null;
		for (int i = 0; i < tempList.length; ++i) {
			if (path.endsWith(File.separator))
				temp = new File(path + tempList[i]);
			else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				clearFolder(path + "/" + tempList[i]);
				delFolder(path + "/" + tempList[i]);
				flag = true;
			}
		}
		return flag;
	}

	public static boolean clearFolder(String path, String exceptFileName) {
		File file = new File(path);
		if (!(file.exists())) {
			return true;
		}
		if (!(file.isDirectory())) {
			return true;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; ++i) {
			if (path.endsWith(File.separator))
				temp = new File(path + tempList[i]);
			else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if ((temp.isFile()) && (!(temp.getName().equals(exceptFileName))) && (!(temp.delete()))) {
				return false;
			}

			if (temp.isDirectory()) {
				if (!(clearFolder(path + "/" + tempList[i]))) {
					return false;
				}
				if (!(delFolder(path + "/" + tempList[i]))) {
					return false;
				}
			}
		}

		return true;
	}

	public static boolean delFolder(String folderPath) {
		try {
			clearFolder(folderPath);
			String filePath = folderPath;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			myFilePath.delete();
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public static void createFile(String filename) {
		File file = new File(filename);
		if (file.exists())
			return;
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void createFile(String dic, String filename) {
		File file = new File(dic, filename);
		if (file.exists())
			return;
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isFileExisted(String dic, String fileName) {
		File file = new File(dic, fileName);
		return file.exists();
	}

	public static List<String> getAllFolderPath(String path) {
		List list = new ArrayList();
		getAllFolderPath(list, path);
		return list;
	}

	public static void getAllFolderPath(List<String> list, String path) {
		File file = new File(path);

		File[] files = file.listFiles();

		for (File one : files) {
			if (one.isDirectory()) {
				list.add(one.getAbsolutePath());
				getAllFolderPath(list, one.getAbsolutePath());
			}
		}
	}

	public static int getAllFilesCount(String folder) {
		return getAllFileNames(folder).length;
	}

	public static String[] getAllFileNames(String folder) {
		LinkedList list = new LinkedList();
		LinkedHashSet files = new LinkedHashSet();
		File dir = new File(folder);

		File[] file = dir.listFiles();
		if (file == null) {
			return ((String[]) files.toArray(new String[0]));
		}
		for (int i = 0; i < file.length; ++i) {
			if (file[i].isDirectory())
				list.add(file[i]);
			else {
				files.add(file[i].getAbsolutePath());
			}
		}

		while (!(list.isEmpty())) {
			File tmp = (File) list.removeFirst();
			if (tmp.isDirectory()) {
				file = tmp.listFiles();
				if (file == null) {
					continue;
				}
				for (int i = 0; i < file.length; ++i)
					if (file[i].isDirectory())
						list.add(file[i]);
					else
						files.add(file[i].getAbsolutePath());
			} else {
				files.add(tmp.getAbsolutePath());
			}
		}

		return ((String[]) files.toArray(new String[0]));
	}

	public static boolean deleteFile(String dic, String fileName) {
		File file = new File(dic, fileName);
		if (file.exists()) {
			return file.delete();
		}

		return true;
	}

	public static boolean copyFiles(String sourceDic, String targetDic) {
		try {
			FileUtils.copyDirectory(new File(sourceDic), new File(targetDic));
		} catch (IOException e) {
			return false;
		}

		return true;
	}

	public static String[] getFilesUnderFolder(String folder) {
		List list = new ArrayList();
		File f = new File(folder);
		File[] fs = f.listFiles();
		if (fs != null) {
			for (File file : fs) {
				if (!(file.isDirectory())) {
					list.add(file.getAbsolutePath());
				}
			}
		}

		return ((String[]) list.toArray(new String[0]));
	}

	public static String[] getFoldersUnderFolder(String folder) {
		List list = new ArrayList();
		File f = new File(folder);
		File[] fs = f.listFiles();
		if (fs != null) {
			for (File file : fs) {
				if ((!(file.isDirectory())) || (file.getAbsolutePath().equals(f.getAbsolutePath())))
					continue;
				list.add(file.getAbsolutePath());
			}

		}

		return ((String[]) list.toArray(new String[0]));
	}

	public static long getLastModifiedTime(String folder) {
		File f = new File(folder);
		File[] fileArray = f.listFiles();
		long lastmodified = -1L;
		if (fileArray != null) {
			for (int i = 0; i < fileArray.length; ++i) {
				if (lastmodified < fileArray[i].lastModified()) {
					lastmodified = fileArray[i].lastModified();
				}
			}
		}
		return lastmodified;
	}

	public static void output(Hashtable<String, Integer> list, int condition, String filePath) {
		output(list, condition, false, filePath);
	}

	public static void output(Hashtable<String, Integer> list, int condition, boolean append, String filePath) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filePath), append));
			Iterator it = list.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				if (((Integer) list.get(key)).intValue() > condition) {
					bw.write(key + "," + list.get(key));
					bw.write("\n");
				}
			}
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getFileContent(String fileName, String encoding) {
		return StringUtil.getStringFromStrings(getContentByLine(fileName, encoding), "\n").trim();
	}

	public static void output(Collection<String> list, String filePath, String encoding) {
		output((String[]) list.toArray(new String[0]), filePath, encoding);
	}

	public static void output(String[] list, String filePath, String encoding) {
		try {
			BufferedWriter bw = getWriter(filePath, encoding);
			if (bw != null) {
				if (list.length > 0) {
					bw.write(list[0]);
					for (int i = 1; i < list.length; ++i) {
						if (list[i] != null) {
							bw.write("\n");
							bw.write(list[i]);
						}
					}
				}
				bw.close();
			}
		} catch (Exception localException) {
		}
	}

	public static BufferedWriter getWriter(String filePath, String encoding) {
		try {
			return new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(new File(filePath), false), encoding));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException localFileNotFoundException) {
		}
		return null;
	}

	public static BufferedReader getReader(String filePath, String encoding) {
		try {
			return new BufferedReader(new InputStreamReader(new FileInputStream(filePath), encoding));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}
}
