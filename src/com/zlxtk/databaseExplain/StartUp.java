package com.zlxtk.databaseExplain;

import java.util.List;
import java.util.Map;

/**
 * 启动类
 *
 * @see com.zlxtk.databaseExplain.StartUp
 * @author ZLXTK
 * @date 2017年1月11日
 * @Version 1.0
 *
 */
public class StartUp {

	public static void main(String[] args) {
		try {
			// 数据库操作类
			DataServiceImpl db = new DataServiceImpl();
			// excel文件操作类
			ExcelServiceImpl ex = new ExcelServiceImpl();

			// 查询数据库所有的表
			List<Map<String, String>> tables = db.getTables();

			// 开始写入各个表中的数据
			for (Map<String, String> map : tables) {
				String name = map.get("1_NAME");
				List<Map<String, String>> list = db.getTableColumnProperty(name);
				System.out.println("开始写入表:" + name + " 的数据...");
				ex.WriteDateToSheel(list, name, 0);
			}

			System.out.println("写入汇总页...");
			ex.WriteDateToSheel(tables, "All_tables", 1);
			System.out.println("汇总页写入完成!");

			System.out.println("\n所有工作完成！！！！");

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
