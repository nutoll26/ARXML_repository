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

	public ExtractExcel() {
		xlsxWb = new XSSFWorkbook();
	}

	public void createExcelFile(String ecuName) {
		try {
			File xlsFile = new File("/Users/shinwoochul/Desktop/JOLP/" + ecuName + "_Excel.xlsx");
			FileOutputStream fileOut = new FileOutputStream(xlsFile);
			xlsxWb.write(fileOut);
			System.out.println("complete extraction excel");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createDelegationSheet(ResourceSet queryResult, String ecuName) throws XMLDBException {

		ResourceIterator iter = queryResult.getIterator();

		int rowNumber = 0;

		// Sheet 생성
		Sheet delegationSheet = xlsxWb.createSheet("Delegation");

		// 컬럼 너비 설정
		delegationSheet.setColumnWidth(0, 2000);
		delegationSheet.setColumnWidth(1, 7000);
		delegationSheet.setColumnWidth(2, 8000);
		delegationSheet.setColumnWidth(3, 4000);
		delegationSheet.setColumnWidth(4, 5000);
		delegationSheet.setColumnWidth(5, 10000);
		delegationSheet.setColumnWidth(6, 10000);
		delegationSheet.setColumnWidth(7, 10000);
		delegationSheet.setColumnWidth(8, 10000);

		// Cell 스타일 생성
		CellStyle cellStyle = xlsxWb.createCellStyle();

		// 줄 바꿈
		cellStyle.setWrapText(true);

		// Cell 색깔, 무늬 채우기
		cellStyle.setFillForegroundColor(HSSFColor.LIME.index);
		cellStyle.setFillPattern(CellStyle.BIG_SPOTS);

		Row row = null;
		Cell cell = null;

		row = delegationSheet.createRow(rowNumber++);

		cell = row.createCell(0);
		cell.setCellValue("No");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(1);
		cell.setCellValue("PortIterface\nShortName");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(2);
		cell.setCellValue("PortInterface\nCategory");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(3);
		cell.setCellValue("DataElement\nName");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(4);
		cell.setCellValue("DataType");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(5);
		cell.setCellValue("DataType\nCategory");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(6);
		cell.setCellValue("Shortname\nof Port");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(7);
		cell.setCellValue("Port Category");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(8);
		cell.setCellValue("Atomic SWC");
		cell.setCellStyle(cellStyle);

		int numbering = 1;
		while (iter.hasMoreResources()) {
			org.xmldb.api.base.Resource r = iter.nextResource();
			String str = (String) r.getContent();
			StringTokenizer token = new StringTokenizer(str, ",");

			row = delegationSheet.createRow(rowNumber++);
			cell = row.createCell(0);
			cell.setCellValue(numbering++);

			if (token.countTokens() == 8) {
				int c = 1;

				while (token.hasMoreTokens()) {
					String value = token.nextToken();

					cell = row.createCell(c++);
					cell.setCellValue(value);
				}
			} else if (token.countTokens() == 7) {
				int c = 1;

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
								row = delegationSheet.createRow(rowNumber++);
								cell = row.createCell(7);
							}
						}

						if (tokenCount > 1) {
							for (int mi = 0; mi < 7; mi++) {
								delegationSheet.addMergedRegion(new CellRangeAddress(rowNumber - tokenCount,
										rowNumber - (tokenCount - 1), mi, mi));
							}
						}
					} else { // 앞의 엑셀 셀에 내용
						cell.setCellValue(value);
					}
				}
			} else {
				int c = 1;
				while (token.hasMoreTokens()) {
					String value = token.nextToken();

					cell = row.createCell(c++);
					cell.setCellValue(value);
				}
				while (c != 9) {
					cell = row.createCell(c++);
					cell.setCellValue("NULL");
				}
			}
		}
	}

	public void createAssemblySheet(ResourceSet queryResult, String ecuName) throws XMLDBException {

		ResourceIterator iter = queryResult.getIterator();

		if (iter.hasMoreResources()) {
			int rowNumber = 0;

			Sheet assemblySheet = xlsxWb.createSheet("Assembly");

			assemblySheet.setColumnWidth(0, 2000);
			assemblySheet.setColumnWidth(1, 7000);
			assemblySheet.setColumnWidth(2, 8000);
			assemblySheet.setColumnWidth(3, 5500);
			assemblySheet.setColumnWidth(4, 6500);

			CellStyle cellStyle = xlsxWb.createCellStyle();

			cellStyle.setWrapText(true);

			cellStyle.setFillForegroundColor(HSSFColor.LIME.index);
			cellStyle.setFillPattern(CellStyle.BIG_SPOTS);

			Row row = null;
			Cell cell = null;

			// 첫 번째 줄
			row = assemblySheet.createRow(rowNumber++);

			// 첫 번째 줄에 Cell 설정하기-------------
			cell = row.createCell(0);
			cell.setCellValue("No");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(1);
			cell.setCellValue("PortIterface\nShortName");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(2);
			cell.setCellValue("Shortname \nof Port");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(3);
			cell.setCellValue("Port\nCategory");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(4);
			cell.setCellValue("Atomic SWC");
			cell.setCellStyle(cellStyle);

			int numbering = 1;
			while (iter.hasMoreResources()) {
				org.xmldb.api.base.Resource r = iter.nextResource();
				String str = (String) r.getContent();

				StringTokenizer token = new StringTokenizer(str, ",");

				row = assemblySheet.createRow(rowNumber++);
				cell = row.createCell(0);
				cell.setCellValue(numbering++);

				int c = 1;
				while (token.hasMoreTokens()) {
					if (c == 5) {
						row = assemblySheet.createRow(rowNumber++);
						cell = row.createCell(0);
						cell.setCellValue(numbering++);

						c = 1;
					}
					String value = token.nextToken();

					cell = row.createCell(c++);
					cell.setCellValue(value);
				}
			}
		}
	}

	public void createContinValueSheet(ResourceSet queryResult, String ecuName) throws XMLDBException {
		ResourceIterator iter = queryResult.getIterator();

		if (iter.hasMoreResources()) {
			int rowNumber = 0;

			Sheet delegationSheet = xlsxWb.createSheet("ContinuousValue");

			delegationSheet.setColumnWidth(0, 2000);
			delegationSheet.setColumnWidth(1, 3500);
			delegationSheet.setColumnWidth(2, 4000);
			delegationSheet.setColumnWidth(3, 7000);
			delegationSheet.setColumnWidth(4, 4000);
			delegationSheet.setColumnWidth(5, 4000);
			delegationSheet.setColumnWidth(6, 4000);
			delegationSheet.setColumnWidth(7, 4000);

			CellStyle cellStyle = xlsxWb.createCellStyle();

			cellStyle.setWrapText(true);

			cellStyle.setFillForegroundColor(HSSFColor.LIME.index);
			cellStyle.setFillPattern(CellStyle.BIG_SPOTS);

			Row row = null;
			Cell cell = null;

			row = delegationSheet.createRow(rowNumber++);

			cell = row.createCell(0);
			cell.setCellValue("No");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(1);
			cell.setCellValue("ShortName");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(2);
			cell.setCellValue("LongName");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(3);
			cell.setCellValue("Description");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(4);
			cell.setCellValue("Resolution");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(5);
			cell.setCellValue("Physical\nLower\nLimit");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(6);
			cell.setCellValue("Physical\nUpper\nLimit");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(7);
			cell.setCellValue("Unit");
			cell.setCellStyle(cellStyle);

			int numbering = 1;
			while (iter.hasMoreResources()) {
				org.xmldb.api.base.Resource r = iter.nextResource();
				String str = (String) r.getContent();
				StringTokenizer token = new StringTokenizer(str, ",");

				row = delegationSheet.createRow(rowNumber++);
				cell = row.createCell(0);
				cell.setCellValue(numbering++);

				int c = 1;
				while (token.hasMoreTokens()) {
					String value = token.nextToken();

					cell = row.createCell(c++);
					cell.setCellValue(value);
				}

			}
		}
	}

	public void createEnumerationSheet(ResourceSet queryResult, String ecuName) throws XMLDBException {
		ResourceIterator iter = queryResult.getIterator();

		if (iter.hasMoreResources()) {
			int rowNumber = 0;

			Sheet assemblySheet = xlsxWb.createSheet("Enumeration");

			assemblySheet.setColumnWidth(0, 8000);
			assemblySheet.setColumnWidth(1, 8000);
			assemblySheet.setColumnWidth(2, 8000);
			assemblySheet.setColumnWidth(3, 5500);
			assemblySheet.setColumnWidth(4, 5500);
			assemblySheet.setColumnWidth(5, 5500);

			CellStyle cellStyle = xlsxWb.createCellStyle();

			cellStyle.setWrapText(true);

			cellStyle.setFillForegroundColor(HSSFColor.LIME.index);
			cellStyle.setFillPattern(CellStyle.BIG_SPOTS);

			Row row = null;
			Cell cell = null;

			// 첫 번째 줄
			row = assemblySheet.createRow(rowNumber++);

			// 첫 번째 줄에 Cell 설정하기-------------
			cell = row.createCell(0);
			cell.setCellValue("Data type\nname");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(1);
			cell.setCellValue("Long Name");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(2);
			cell.setCellValue("Description");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(3);
			cell.setCellValue("value");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(4);
			cell.setCellValue("name");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(5);
			cell.setCellValue("comment");
			cell.setCellStyle(cellStyle);

			while (iter.hasMoreResources()) {
				org.xmldb.api.base.Resource r = iter.nextResource();
				String str = (String) r.getContent();

				StringTokenizer token = new StringTokenizer(str, "@");

				row = assemblySheet.createRow(rowNumber++);

				int c = 0;
				while (token.hasMoreTokens()) {
					String value = token.nextToken();

					if (c == 3) {
						StringTokenizer token2 = new StringTokenizer(value, "#");

						String value2 = token2.nextToken();

						StringTokenizer token3 = new StringTokenizer(value2);

						cell = row.createCell(3);
						cell.setCellValue(token3.nextToken());
						token3.nextToken();
						cell = row.createCell(4);
						cell.setCellValue(token3.nextToken());

						if (token3.hasMoreTokens()) {
							cell = row.createCell(5);
							String strAppend = "";

							while (token3.hasMoreTokens()) {
								strAppend += token3.nextToken();
								strAppend += " ";
							}
							cell.setCellValue(strAppend);
						}

						while (token2.hasMoreTokens()) {
							row = assemblySheet.createRow(rowNumber++);

							value2 = token2.nextToken();

							token3 = new StringTokenizer(value2);

							cell = row.createCell(3);
							cell.setCellValue(token3.nextToken());
							token3.nextToken();
							cell = row.createCell(4);
							cell.setCellValue(token3.nextToken());

							if (token3.hasMoreTokens()) {
								cell = row.createCell(5);
								String strAppend = "";

								while (token3.hasMoreTokens()) {
									strAppend += token3.nextToken();
									strAppend += " ";
								}
								cell.setCellValue(strAppend);
							}
						}
						c = 0;
					} else {
						cell = row.createCell(c++);
						cell.setCellValue(value);
					}
				}
			}
		}
	}

	public void createRecordSheet(ResourceSet queryResult, String ecuName) throws XMLDBException {
		ResourceIterator iter = queryResult.getIterator();

		if (iter.hasMoreResources()) {
			int rowNumber = 0;

			Sheet assemblySheet = xlsxWb.createSheet("Record");

			assemblySheet.setColumnWidth(0, 6000);
			assemblySheet.setColumnWidth(1, 6000);
			assemblySheet.setColumnWidth(2, 8000);
			assemblySheet.setColumnWidth(3, 3000);
			assemblySheet.setColumnWidth(4, 5500);
			assemblySheet.setColumnWidth(5, 5500);
			assemblySheet.setColumnWidth(6, 8000);

			CellStyle cellStyle = xlsxWb.createCellStyle();

			cellStyle.setWrapText(true);

			cellStyle.setFillForegroundColor(HSSFColor.LIME.index);
			cellStyle.setFillPattern(CellStyle.BIG_SPOTS);

			Row row = null;
			Cell cell = null;

			// 첫 번째 줄
			row = assemblySheet.createRow(rowNumber++);

			// 첫 번째 줄에 Cell 설정하기-------------
			cell = row.createCell(0);
			cell.setCellValue("Record type\nname");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(1);
			cell.setCellValue("Long Name");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(2);
			cell.setCellValue("Description");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(3);
			cell.setCellValue("Number of\nelement");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(4);
			cell.setCellValue("Name");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(5);
			cell.setCellValue("Type name");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(6);
			cell.setCellValue("Comment");
			cell.setCellStyle(cellStyle);

			while (iter.hasMoreResources()) {
				org.xmldb.api.base.Resource r = iter.nextResource();
				String str = (String) r.getContent();

				StringTokenizer token = new StringTokenizer(str, "@");

				row = assemblySheet.createRow(rowNumber++);

				int c = 0;
				while (token.hasMoreTokens()) {
					String value = token.nextToken();

					if (c == 4) {
						StringTokenizer token2 = new StringTokenizer(value, "#");
						String value2 = token2.nextToken();
						
						StringTokenizer token3 = new StringTokenizer(value2);
						
						if(token3.countTokens() == 2){	
							cell = row.createCell(4);
							cell.setCellValue(token3.nextToken());
							
							cell = row.createCell(5);
							cell.setCellValue(token3.nextToken());
						}else{	
							cell = row.createCell(4);
							cell.setCellValue(token3.nextToken());
							
							cell = row.createCell(5);
							cell.setCellValue(token3.nextToken());
							
							cell = row.createCell(6);
							String temp = "";
							while(token3.hasMoreTokens()){
								temp += token3.nextToken();
								temp += " ";
							}
							cell.setCellValue(temp);
						}
						
						while (token2.hasMoreTokens()) {
							row = assemblySheet.createRow(rowNumber++);
							
							value2 = token2.nextToken();
							
							token3 = new StringTokenizer(value2);
							if(token3.countTokens() == 2){	
								cell = row.createCell(4);
								cell.setCellValue(token3.nextToken());
								
								cell = row.createCell(5);
								cell.setCellValue(token3.nextToken());
							}else{	
								cell = row.createCell(4);
								cell.setCellValue(token3.nextToken());
								
								cell = row.createCell(5);
								cell.setCellValue(token3.nextToken());
								
								cell = row.createCell(6);
								String temp = "";
								while(token3.hasMoreTokens()){
									temp += token3.nextToken();
									temp += " ";
								}
								cell.setCellValue(temp);
							}
						}
						c = 0;
					} else {
						cell = row.createCell(c++);
						cell.setCellValue(value);
					}
				}
			}
		}
	}
}
