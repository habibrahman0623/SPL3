package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadWriteOnExcelFile {
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String excelFilePath = "dataset.xlsx";
        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
        PrintWriter bugReportWriter = new PrintWriter(new File("BugReport.txt"));
        PrintWriter reportTypeWriter = new PrintWriter(new File("reportType.txt"));
        
        XSSFRow row;
        XSSFCell cell;
        Workbook workbook = new XSSFWorkbook(inputStream);
        int sheetNumber = workbook.getNumberOfSheets();
        for(int i = 0; i < sheetNumber; i++){
        	
        	Sheet sheet = workbook.getSheetAt(i);
            Iterator<Row> iterator = sheet.iterator();
            int rows = sheet.getPhysicalNumberOfRows();
            int cols = sheet.getRow(0).getPhysicalNumberOfCells();
            String report;
            String processedString = "";
            for(int r = 1; r < rows; r++) {
                row = (XSSFRow) sheet.getRow(r);
                if(row != null) {
                	
                		processedString = preprocessing(row.getCell(13).getStringCellValue());
                		bugReportWriter.println(processedString);
                		bugReportWriter.flush();
                		reportTypeWriter.println(row.getCell(1).getStringCellValue());
                			
                		
                		reportTypeWriter.flush();
                	/*report = row.getCell(1).getStringCellValue() + ":" + row.getCell(13).getStringCellValue();
                	writer.println(report);*/
                	//System.out.println(row.getCell(0));
                	
                }
                
            }         

        }
        workbook.close();
        inputStream.close();
	}
	
	public static String preprocessing(String report){
		
		String[] tokens = report.split("\\s+");
		String processedString = "";
		for (String word : tokens) {
			//if(!(word.contains("/")||word.contains("."))){
				if(word.toLowerCase().contains("error")){
					word = "error";
				}
				else if(word.toLowerCase().contains("exception")){
					word = "exception";
				}
				processedString = processedString + word + " ";
			//}
		}
		
		return processedString;
		
		
	}


}
