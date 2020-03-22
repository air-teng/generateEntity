package gEntity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import gEntity.util.IOUtil;
import gEntity.util.JDBCUtil;
import gEntity.util.StringUtil;

public class GenerateEntity {
	private static final String PROJECT_ROOT = System.getProperty("user.dir");
	private static final String DB_FILE_PATH = PROJECT_ROOT + "\\src\\dbFile.properties";
	private static final String DB_FILE_TYPE = PROJECT_ROOT + "\\src\\filedTypeMap";
	private static final String TABLE_NAME = PROJECT_ROOT + "\\src\\tableName";
	private static final String COMMENT_SIGNATURE = "#";
	private static final String CONFIG_SPLITER = "=";
	private static final String TARGET_FILE = "filepath";
	private static final String FILE_SPERATOR = "\\";

	public static void main(String[] args) {
		// 读取配置
		Map<String, String> configMap = readFileToMap(DB_FILE_PATH);
		// 读取类型
		Map<String, String> fieldTypeMap = readFileToMap(DB_FILE_TYPE);
		// 读取表格
		Map<String, String> tableNameMap = readFileToMap(TABLE_NAME);
		// 获取连接
		Connection conn = JDBCUtil.getJdbcConn(configMap);
		
		String classPackageRootPath = PROJECT_ROOT+FILE_SPERATOR+configMap.get(TARGET_FILE);
		String packageName = String.join(".", configMap.get(TARGET_FILE).split("/"));
		for (String tableName : tableNameMap.keySet()) {
			String entityClassName = StringUtil.underLineToCamel(tableNameMap.get(tableName));
			String fileName = classPackageRootPath+FILE_SPERATOR+entityClassName+".java";
			BufferedWriter writer = IOUtil.getWriter(fileName);
			JDBCUtil.buildPoByTable(tableName, conn, fieldTypeMap, writer, packageName,entityClassName);
			System.out.println("generate ["+entityClassName+".java] success!");
		}
		
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 读取配置
	private static Map<String, String> readFileToMap(String fileName) {
		Map<String, String> map = new HashMap<>();
		try {
			BufferedReader reader = IOUtil.getReader(fileName);

			String tempStr = "";
			while ((tempStr = reader.readLine()) != null) {
				if (tempStr.trim().isEmpty() || tempStr.trim().startsWith(COMMENT_SIGNATURE)) {
					continue;
				}

				String[] split = tempStr.split(CONFIG_SPLITER);
				map.put(split[0].trim(), split[1].trim());
			}

			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	
}
