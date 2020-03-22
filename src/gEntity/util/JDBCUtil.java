package gEntity.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class JDBCUtil {
	private static final String USERNAME = "username";
	private static final String PASSWORD_NAME = "password";
	private static final String URL = "url";
	private static final String DRIVER_NAME = "driver";

	/**
	 * 获得数据库连接
	 * 
	 * @return Connection
	 */
	public static Connection getJdbcConn(Map<String, String> configMap) {
		Connection con = null;
		try {
			String url = configMap.get(URL);
			String username = configMap.get(USERNAME);
			String password = configMap.get(PASSWORD_NAME);
			String driverName = configMap.get(DRIVER_NAME);
			Class.forName(driverName);
			// 获取数据库连接
			con = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;

	}

	/**
	 * 以_分割的，会以_分割，分割后的字符串数组从第二个字符串开始首字母大写，然后再组成最终的属性名称
	 * 
	 * @param tableName
	 *            表名
	 * @param conn
	 *            连接
	 * @param fieldTypeMap
	 *            域映射
	 * @param writer
	 *            目标文件 书写的目标文件
	 */
	public static void buildPoByTable(String tableName, Connection conn, Map<String, String> fieldTypeMap,
			BufferedWriter writer, String packageName, String className) {

		writeClassByLombok(writer, tableName, packageName, className);

		Statement st = null;
		ResultSet rs = null;
		//为了防止表名使用关键字导致语法错误，需要加上 ``
		String sql = "SELECT * FROM `" + tableName+"`";
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			ResultSetMetaData md = rs.getMetaData();
			for (int i = 1; i <= md.getColumnCount(); i++) {
				String columnName = md.getColumnName(i).toLowerCase();
				String columnType = md.getColumnTypeName(i).toLowerCase();

				// convert underline to Camel
				columnName = StringUtil.underLineToCamel(columnName);

				if ("VERSION_".equals(columnName)) {
					continue;
				} else {
					writer.write("	private " + fieldTypeMap.get(columnType.toLowerCase()) + " " + columnName + ";\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			writer.write("\n}");
			// 将缓冲区中的数据全部写入
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeClassByLombok(BufferedWriter writer, String tableName, String packageName,
			String className) {
		try {
			writer.write("package " + packageName + ";\n\n");
			writer.write("@Data\n");
			writer.write("@ToString\n");
			writer.write("public class " + className + "{\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
