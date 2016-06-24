package com.asura.tools.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeUtil {
	public static Object ByteToObject(byte[] bytes) {
		Object obj = null;
		try {
			ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
			ObjectInputStream oi = new ObjectInputStream(bi);

			obj = oi.readObject();

			bi.close();
			oi.close();
		} catch (Exception e) {
			System.out.println("serialize exception:" + ExceptionUtil.getExceptionContent(e));
			e.printStackTrace();
		}
		return obj;
	}

	public static String ObjectToByteString(Object obj) {
		byte[] bytes = ObjectToByte(obj);
		StringBuffer sb = new StringBuffer();
		for (byte bt : bytes) {
			sb.append(bt + " ");
		}

		return sb.toString().trim();
	}

	public static Object ByteStringToObject(String byteString) {
		String[] ss = byteString.trim().split(" ");
		byte[] bytes = new byte[ss.length];
		for (int i = 0; i < ss.length; ++i) {
			bytes[i] = Byte.valueOf(ss[i]).byteValue();
		}

		return ByteToObject(bytes);
	}

	public static byte[] ObjectToByte(Object obj) {
		byte[] bytes = null;
		try {
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);

			bytes = bo.toByteArray();

			bo.close();
			oo.close();
		} catch (Exception e) {
			System.out.println("translation" + e.getMessage());
			e.printStackTrace();
		}
		return bytes;
	}

	public static void writeObject(Object o, String fileName) {
		File f = new File(fileName);
		if (f.exists()) {
			f.delete();
		}
		try {
			FileOutputStream os = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(o);
			oos.close();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Object readObject(String f) {
		try {
			InputStream is = new FileInputStream(new File(f));
			ObjectInputStream ois = new ObjectInputStream(is);

			return ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Object readObject(InputStream f) {
		try {
			InputStream is = f;
			ObjectInputStream ois = new ObjectInputStream(is);

			return ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
