package kr.ac.kookmin.cs.javaToExcel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;

public class ExtractExcel {

	// Workbook 생성
	private Workbook xlsxWb = null;

	public void createDelegationSheet(ResourceSet queryResult, String ecuName) throws XMLDBException {

		xlsxWb = new XSSFWorkbook(); // Excel 2007 이상
		
		ResourceIterator iter = queryResult.getIterator();
		int rowNumber = 0;

		// *** Sheet-------------------------------------------------
		// Sheet 생성
		Sheet sheet1 = xlsxWb.createSheet("Delegation");

		// 컬럼 너비 설정
		sheet1.setColumnWidth(0, 1000);
		sheet1.setColumnWidth(1, 7000);
		sheet1.setColumnWidth(2, 8000);
		sheet1.setColumnWidth(3, 4000);
		sheet1.setColumnWidth(4, 5000);
		sheet1.setColumnWidth(5, 10000);
		sheet1.setColumnWidth(6, 10000);
		sheet1.setColumnWidth(7, 10000);
		sheet1.setColumnWidth(8, 10000);
		// ----------------------------------------------------------

		// *** Style--------------------------------------------------
		// Cell 스타일 생성
		CellStyle cellStyle = xlsxWb.createCellStyle();

		// 줄 바꿈
		cellStyle.setWrapText(true);

		// Cell 색깔, 무늬 채우기
		cellStyle.setFillForegroundColor(HSSFColor.LIME.index);
		cellStyle.setFillPattern(CellStyle.BIG_SPOTS);

		Row row = null;
		Cell cell = null;
		// ----------------------------------------------------------

		// 첫 번째 줄
		row = sheet1.createRow(rowNumber++);

		// 첫 번째 줄에 Cell 설정하기-------------
		cell = row.createCell(0);
		cell.setCellValue("No");
		cell.setCellStyle(cellStyle); // 셀 스타일 적용

		cell = row.createCell(1);
		cell.setCellValue("PortIterface\nShortName");
		cell.setCellStyle(cellStyle); // 셀 스타일 적용

		cell = row.createCell(2);
		cell.setCellValue("PortInterface\nCategory");
		cell.setCellStyle(cellStyle); // 셀 스타일 적용

		cell = row.createCell(3);
		cell.setCellValue("DataElement\nName");
		cell.setCellStyle(cellStyle); // 셀 스타일 적용

		cell = row.createCell(4);
		cell.setCellValue("DataType");
		cell.setCellStyle(cellStyle); // 셀 스타일 적용

		cell = row.createCell(5);
		cell.setCellValue("DataType\nCategory");
		cell.setCellStyle(cellStyle); // 셀 스타일 적용

		cell = row.createCell(6);
		cell.setCellValue("Shortname\nof Port");
		cell.setCellStyle(cellStyle); // 셀 스타일 적용

		cell = row.createCell(7);
		cell.setCellValue("Port Category");
		cell.setCellStyle(cellStyle); // 셀 스타일 적용

		cell = row.createCell(8);
		cell.setCellValue("Atomic SWC");
		cell.setCellStyle(cellStyle);
		// ---------------------------------

		int numbering = 1;
		while (iter.hasMoreResources()) {
			org.xmldb.api.base.Resource r = iter.nextResource();
			String str = (String) r.getContent();
			StringTokenizer token = new StringTokenizer(str, ",");

			System.out.println(str);
			row = sheet1.createRow(rowNumber++);
			cell = row.createCell(0);
			cell.setCellValue(numbering++);

			if (token.countTokens() == 8) {
				System.out.println("8888");
				int c = 1;
				while (token.hasMoreTokens()) {
					String value = token.nextToken();

					cell = row.createCell(c++);
					cell.setCellValue(value);
				}
			} else if (token.countTokens() == 7) {
				int c = 1;
				System.out.println("7777");
				
				while (token.hasMoreTokens()) {
					String value = token.nextToken();
					
					cell = row.createCell(c++);
					if (c == 8) {
						StringTokenizer tokenBySpace = new StringTokenizer(value);
						int tokenCount = tokenBySpace.countTokens();
						
						while (tokenBySpace.hasMoreTokens()) {
							String item = tokenBySpace.nextToken();
							StringTokenizer tokenByColon = new StringTokenizer(item, ":");

							for (int ci = 0; ci < tokenByColon.countTokens(); ci++) {
								cell.setCellValue(tokenByColon.nextToken());
								Cell cell2 = row.createCell(8);
								cell2.setCellValue(tokenByColon.nextToken());
							}

							if (tokenBySpace.hasMoreTokens()) {
								row = sheet1.createRow(rowNumber++);
								cell = row.createCell(7);
							}
						}
						
						if(tokenCount > 1){
							for(int mi=0; mi<7; mi++){
								sheet1.addMergedRegion(new CellRangeAddress(rowNumber-tokenCount,
										rowNumber-(tokenCount-1), mi, mi));	
							}
						}
					} else {	// 앞의 엑셀 셀에 내용
						cell.setCellValue(value);
					}
				}
			}
			else{
				int c = 1;
				while (token.hasMoreTokens()) {
					String value = token.nextToken();

					cell = row.createCell(c++);
					cell.setCellValue(value);
				}
				while(c != 9){
					cell = row.createCell(c++);
					cell.setCellValue("NULL");
				}
			}
		}

		try {
			File xlsFile = new File("/Users/shinwoochul/Desktop/JOLP/"+ecuName+"_Excel.xlsx");
			FileOutputStream fileOut = new FileOutputStream(xlsFile);
			xlsxWb.write(fileOut);
			System.out.println("complete extraction excel");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			xlsxWb = null;
		}
	}
}
