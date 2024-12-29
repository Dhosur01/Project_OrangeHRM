package Scenario1HRM1;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
    private static Workbook workbook;
    private static Sheet sheet;

    // Method to set the Excel file and sheet
    public static void setExcelFile(String excelFilePath, String sheetName) throws IOException {
        FileInputStream inputStream = new FileInputStream(excelFilePath);
        workbook = new XSSFWorkbook(inputStream);
        sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            throw new IllegalArgumentException("Sheet \"" + sheetName + "\" does not exist in " + excelFilePath);
        }
    }

    // Method to get cell data
    public static String getCellData(int rowNum, int colNum) {
        Row row = sheet.getRow(rowNum);
        Cell cell = row.getCell(colNum);
        return cell.getStringCellValue();
    }

    // Method to get row count
    public static int getRowCount() {
        return sheet.getLastRowNum();
    }

    // Method to close the Excel file
    public static void closeExcelFile() throws IOException {
        if (workbook != null) {
            workbook.close();
        }
    }
}
