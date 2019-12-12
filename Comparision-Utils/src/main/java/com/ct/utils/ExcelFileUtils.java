package com.ct.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelFileUtils {

	private Workbook workBook = new HSSFWorkbook();
	private Sheet sheet;
	private OutputStream fileOut;

	private ExcelFileUtils() {
		sheet = workBook.createSheet();
	}

	public static ExcelFileUtils fileLocation(String fileName, String dirLocation) {
		ExcelFileUtils utils = new ExcelFileUtils();
		try {
			String fileWithLocation = null;
			if (fileName != null && !fileName.endsWith(".xlsx")) {
				fileName = fileName + ".xlsx";
			}
			if (dirLocation == null || dirLocation.equals("")) {
				fileWithLocation = fileName;
			} else {
				fileWithLocation = dirLocation + fileName;
			}
			utils.fileOut = new FileOutputStream(fileWithLocation);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return utils;
	}

	public ExcelFileUtils sheetName(String name) {
		this.sheet = workBook.createSheet(name);
		return this;
	}

	public ExcelFileUtils addValueInRow(int rowNo, int cellNo, String value) {
		Row row = null;
		Cell cell = null;
		if (sheet.getRow(rowNo) == null) {
			row = sheet.createRow(rowNo);
		} else {
			row = sheet.getRow(rowNo);
		}

		if (row.getCell(cellNo) == null) {
			cell = row.createCell(cellNo);
		} else {
			cell = row.getCell(cellNo);
		}

		cell.setCellValue(value);
		return this;
	}

	public void write() {
		try {
			workBook.write(fileOut);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileOut.close();
				System.out.println("file created...");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
//		ExcelFileUtils excelUtils = ExcelFileUtils.fileLocation("data", null);
		//Add Header
//		excelUtils.addValueInRow(0,0, "Field List");
		
		//Create Rows
//		for (int i = 1; i < list.size(); i++) {
//			excelUtils.addValueInRow(i, 0, list.get(i));
//		}
//		excelUtils.write();
	}
}
