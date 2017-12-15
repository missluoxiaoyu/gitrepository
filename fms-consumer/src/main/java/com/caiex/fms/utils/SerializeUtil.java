package com.caiex.fms.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;

public class SerializeUtil {
	
	public static byte[] serialize(Object obj) {
		if (obj instanceof String) {
			try {
				return ((String) obj).getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
			}
		}
		
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		byte[] messageBody = null;

		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			messageBody = baos.toByteArray();
			oos.close();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != oos) {
					oos.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (baos != null) {
					baos.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return messageBody;
	}
	
	public static Object deserialize(byte[] messageBody) {
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		Object object = null;
		bais = new ByteArrayInputStream(messageBody);
		try {
			ois = new ObjectInputStream(bais);
			object = ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				ois.close();
				bais.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return object;
	}
}