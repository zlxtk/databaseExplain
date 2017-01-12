package com.zlxtk.databaseExplain;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelServiceImpl {
	private File file;
	private XSSFWorkbook book;
	private static short rowHeight = 20;
	private static short cellWidth = 20;

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

		// 处理类
		CreationHelper createHelper = book.getCreationHelper();

		// 创建表
		XSSFSheet sheet;
		sheet = book.createSheet(sheelName);
		// 设置默认行高和列宽
		sheet.setDefaultRowHeightInPoints(rowHeight);
		sheet.setDefaultColumnWidth(cellWidth);
		// 行号
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
		// 表头列号
		int ce = 0;
		for (String name : names) {
			Cell cell = row.createCell(ce++);
			cell.setCellStyle(getCellStyle(false,rowNum));
			cell.setCellValue(name);
		}

		// 写入数据
		for (Map<String, String> map : tables) {
			row = sheet.createRow(rowNum++);
			// 内容列号
			int cellNum = 0;
			for (String name : names) {
				Cell cell = row.createCell(cellNum++);
				// 样式是否添加超链接
				boolean isLink = false;
				if (type == 1 && name.equals("1_NAME")) {
					Hyperlink hyperlink = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
					// "dmy_user!A1": 链接到名为 dmy_user的sheet页的 A列1行
					hyperlink.setAddress(map.get(name) + "!A1");
					cell.setHyperlink(hyperlink);

					isLink = true;
				}
				cell.setCellStyle(getCellStyle(isLink,rowNum)); // 设置样式，字体颜色
				cell.setCellValue(map.get(name));
			}

		}
		// 写入文件
		FileOutputStream out = getOutputStream();
		book.write(out);
		out.close();
	}

	/**
	 * 设置单元格样式
	 * 
	 * @param isLink
	 *            是否是超链接
	 * @param rowNum
	 *            行号，用于设置背景色
	 * @return
	 * @throws Exception
	 */
	private CellStyle getCellStyle(boolean isLink,int rowNum) throws Exception {
		XSSFCellStyle style = (XSSFCellStyle) book.createCellStyle();
		if (isLink) {
			Font hlink_font = book.createFont();
			hlink_font.setUnderline(Font.U_SINGLE);
			hlink_font.setColor(IndexedColors.BLUE.getIndex());
			style.setFont(hlink_font);
		}

		// 设置单元格边框样式
		// CellStyle.BORDER_DOUBLE 双边线
		// CellStyle.BORDER_THIN 细边线
		// CellStyle.BORDER_MEDIUM 中等边线
		// CellStyle.BORDER_DASHED 虚线边线
		// CellStyle.BORDER_HAIR 小圆点虚线边线
		// CellStyle.BORDER_THICK 粗边线
		style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		
		//设置背景色
		if(rowNum%2==0){
			style.setFillForegroundColor(IndexedColors.ROSE.getIndex()); //设置单元格背景颜色  
		}else{
			style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex()); //设置单元格背景颜色  
		}
		style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND); 
		
		return style;
	}

}
