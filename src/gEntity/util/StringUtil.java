package gEntity.util;

public class StringUtil {
	// 下划线转驼峰命名法
	public static String underLineToCamel(String str) {
		String[] arr = str.split("_");
		for (int i = 1; i < arr.length; i++) {
			if (arr[i].length() > 1) {
				arr[i] = arr[i].substring(0, 1).toUpperCase() + arr[i].substring(1);
			} else {
				arr[i] = arr[i].toUpperCase();
			}
		}
		
		return String.join("", arr);
	}
}
