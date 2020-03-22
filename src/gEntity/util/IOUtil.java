package gEntity.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class IOUtil {

	/**
	 * 获取Writer
	 * 
	 * @param fileName
	 * @return
	 */
	public static BufferedWriter getWriter(String fileName) {
		BufferedWriter bWriter = null;
		try {
			File fileDir = new File(fileName.substring(0,fileName.lastIndexOf("\\")));
			if(!fileDir.exists()) {
				fileDir.mkdirs();
			}
			OutputStreamWriter outWriter = new OutputStreamWriter(new FileOutputStream(fileName));
			bWriter = new BufferedWriter(outWriter);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return bWriter;
	}

	/**
	 * 获取Reader
	 * 
	 * @param fileName
	 * @return BufferedReader
	 */
	public static BufferedReader getReader(String fileName) {
		BufferedReader bReader = null;
		try {
			InputStreamReader outReader = new InputStreamReader(new FileInputStream(fileName));
			bReader = new BufferedReader(outReader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return bReader;
	}
}
