package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class Excel implements AutoCloseable {
    private Workbook workbook;

    public Excel(String filePath) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream("C:\\Users\\engineer\\Desktop\\SQE_ASSIGNMENT_3_22F-3725\\src\\TESTCASES.xlsx")) {
            workbook = new XSSFWorkbook("Sheet1"); // Use XSSFWorkbook for .xlsx files
        }
    }

    public String getCellDataString(String sheetName, int rowIndex, int columnIndex) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            throw new IllegalArgumentException("Sheet " + sheetName + " does not exist");
        }

        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            return ""; // Return an empty string for non-existent rows
        }

        Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        
        // Check cell type and convert to String
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return ""; // Return empty for other types
        }
    }

    @Override
    public void close() throws IOException {
        if (workbook != null) {
            workbook.close();
        }
    }
}
