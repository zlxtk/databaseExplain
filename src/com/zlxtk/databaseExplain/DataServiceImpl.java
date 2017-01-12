package com.zlxtk.databaseExplain;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 数据库操作类，用于查询需要写入的数据
 *
 * @see com.zlxtk.databaseExplain.DataServiceImpl
 * @author ZLXTK
 * @date 2017年1月12日
 * @Version 1.0
 *
 */
public class DataServiceImpl {

	// 驱动程序名
	private static String driver = "com.mysql.jdbc.Driver";

	// 要查询的数据库名
	public String dataBaseName;
	// 要查询的数据库名
	public String url;
	// 数据库用户名
	public String user;
	// 密码
	public String password;

	public DataServiceImpl() throws Exception {
		// TODO Auto-generated constructor stub
		Properties prop = new Properties();
		// 获取项目路径
		File directory = new File("");// 参数为空
		String courseFile = directory.getCanonicalPath() + File.separator + "db.properties";
		// 读取属性文件
		FileInputStream fi = new FileInputStream(courseFile);
		InputStream in = new BufferedInputStream(fi);
		prop.load(in); /// 加载属性列表
		dataBaseName = prop.getProperty("dataBaseName");
		url = prop.getProperty("url") + "/INFORMATION_SCHEMA" + "?characterEncoding=UTF-8&useUnicode=true";
		user = prop.getProperty("user");
		password = prop.getProperty("password");
		in.close();
	}

	/**
	 * 获取连接数据库的链接
	 * 
	 * @return
	 */
	private Connection getConnection() {
		Connection conn = null;
		try {
			// 加载驱动程序
			Class.forName(driver);
			// 连续数据库
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 得到要查询的库的所有表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> getTables() throws Exception {
		List<Map<String, String>> tables = new ArrayList<Map<String, String>>();
		String sql = "SELECT TABLE_NAME name,TABLE_COMMENT comment FROM TABLES WHERE TABLE_SCHEMA='" + dataBaseName + "'";
		Connection conn = getConnection();
		// statement用来执行SQL语句
		Statement statement = conn.createStatement();
		// 结果集
		ResultSet rs = statement.executeQuery(sql);
		while (rs.next()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("1_NAME", rs.getString("name"));
			map.put("2_COMMENT", rs.getString("comment"));
			tables.add(map);
		}
		rs.close();
		conn.close();
		return tables;
	}

	/**
	 * 根据表名查询表各个字段的属性
	 * 
	 * @param tableName
	 *            要查询的表名
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> getTableColumnProperty(String tableName) throws Exception {
		List<Map<String, String>> tables = new ArrayList<Map<String, String>>();
		String sql = "SELECT COLUMN_NAME , DATA_TYPE ,CHARACTER_MAXIMUM_LENGTH ,IS_NULLABLE ," + "COLUMN_DEFAULT ,COLUMN_KEY,COLUMN_COMMENT "
				+ " FROM INFORMATION_SCHEMA.COLUMNS WHERE table_name = '" + tableName + "' " + " AND table_schema = '" + dataBaseName + "'";
		Connection conn = getConnection();
		// statement用来执行SQL语句
		Statement statement = conn.createStatement();
		// 结果集
		ResultSet rs = statement.executeQuery(sql);

		while (rs.next()) {
			//这里把key前缀数字是为了排序
			Map<String, String> map = new HashMap<String, String>();
			map.put("1_COLUMN_NAME", rs.getString("COLUMN_NAME"));
			map.put("2_DATA_TYPE", rs.getString("DATA_TYPE"));
			map.put("3_LENGTH", rs.getString("CHARACTER_MAXIMUM_LENGTH"));
			map.put("4_IS_NULLABLE", rs.getString("IS_NULLABLE"));
			map.put("5_DEFAULT", rs.getString("COLUMN_DEFAULT"));
			map.put("6_COLUMN_KEY", rs.getString("COLUMN_KEY"));
			map.put("7_COMMENT", rs.getString("COLUMN_COMMENT"));
			tables.add(map);
		}
		rs.close();
		conn.close();
		return tables;
	}

	// /**
	// * @param map
	// * @return
	// */
	// private Map<String,String> formatMap(Map<String,String> map){
	// Set<String> set= map.keySet();
	// Iterator<String> it= set.iterator();
	// int num=0;
	// while(it.hasNext()){
	// String key=it.next().toString();
	// map.put(num+"_"+key, map.get(key));
	// num++;
	// map.remove(key);
	// }
	// return map;
	// }
	//
	/**
	 * 测试
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DataServiceImpl ds = new DataServiceImpl();
			System.out.println(ds.dataBaseName);
			System.out.println(ds.user);
			System.out.println(ds.password);

			System.out.println(ds.getTables().size());

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
