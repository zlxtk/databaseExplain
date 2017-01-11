package com.zlxtk.databaseExplain;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelServiceImpl {
	private File file;
	private XSSFWorkbook book;
	private static short rowHight;

	public ExcelServiceImpl() {
		// TODO Auto-generated constructor stub
		try {
			// 创建excel文件
			File f = new File("");
			String dataFile = f.getCanonicalPath() + File.separator + "data.xlsx";
			file = new File(dataFile);
			book = new XSSFWorkbook();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * 获取输出流
	 * 
	 * @return
	 * @throws Exception
	 */
	private FileOutputStream getOutputStream() throws Exception {
		FileOutputStream out = new FileOutputStream(file);
		return out;
	}

	/**
	 * 把tables中的数据写入到名为 sheelName 的页中
	 * 
	 * @param tables
	 *            要写入的数据
	 * @param sheelName
	 *            要写入页的名字
	 * @param type
	 *            动作类型 0：表示各个表的数据，1：表示汇总页，会在各条数据中添加超链接
	 * @throws Exception
	 */
	public void WriteDateToSheel(List<Map<String, String>> tables, String sheelName, int type) throws Exception {

		// 设置超链接单元格样式
		XSSFCellStyle style = book.createCellStyle();
		XSSFFont cellFont = book.createFont();
		cellFont.setUnderline((byte) 1);
		cellFont.setColor(new XSSFColor(Color.BLUE));
		style.setFont(cellFont);

		// 创建表
		XSSFSheet sheet;
		sheet = book.createSheet(sheelName);
		int rowNum = 0;
		// 获取表头
		List<String> names = new ArrayList<String>();
		Set<String> keys = tables.get(0).keySet();
		Iterator<String> it = keys.iterator();
		while (it.hasNext()) {
			String name = it.next().toString();
			names.add(name);
		}
		// 排序
		Collections.sort(names);

		// 写入表头
		XSSFRow row = sheet.createRow(rowNum++);
		// row.setHeight(rowHight);
		int ce = 0;
		for (String name : names) {
			Cell cell = row.createCell(ce++);
			cell.setCellValue(name);
		}

		// 写入数据
		for (Map<String, String> map : tables) {
			row = sheet.createRow(rowNum++);
			// row.setHeight(rowHight);
			int cellNum = 0;
			for (String name : names) {
				Cell cell = row.createCell(cellNum++);
				// 是否添加超链接
				if (type == 1 && name.equals("1_NAME")) {
					CreationHelper createHelper = book.getCreationHelper();
					Hyperlink hyperlink = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
					// "#"表示本文档 "明细页面"表示sheet页名称 "A10"表示第几列第几行
					hyperlink.setAddress(map.get(name) + "!A1");
					cell.setHyperlink(hyperlink);
					cell.setCellStyle(style); // 设置样式，字体颜色
				}
				cell.setCellValue(map.get(name));
			}

		}
		// 写入文件
		FileOutputStream out = getOutputStream();
		book.write(out);
		out.close();
	}

}
