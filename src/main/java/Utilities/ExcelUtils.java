package Utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {

//    // Get data from a sheet and return as a two-dimensional array
//    public static Object[][] getTestData(String filePath, String sheetName) throws IOException {
//        try (FileInputStream file = new FileInputStream(new File(filePath));
//             Workbook workbook = WorkbookFactory.create(file)) {
//            Sheet sheet = workbook.getSheet(sheetName);
//
//            if (sheet == null) {
//                throw new IOException("Sheet " + sheetName + " not found in file " + filePath);
//            }
//
//            int rowCount = sheet.getPhysicalNumberOfRows();
//            if (rowCount == 0) {
//                throw new IOException("No rows found in sheet " + sheetName);
//            }
//
//            Row headerRow = sheet.getRow(0);
//            if (headerRow == null) {
//                throw new IOException("Header row not found in sheet " + sheetName);
//            }
//
//            int colCount = headerRow.getLastCellNum();
//            Object[][] data = new Object[rowCount - 1][colCount]; // Exclude header row
//
//            DataFormatter formatter = new DataFormatter();
//            for (int i = 1; i < rowCount; i++) {
//                Row row = sheet.getRow(i);
//                if (row != null) {
//                    for (int j = 0; j < colCount; j++) {
//                        data[i - 1][j] = formatter.formatCellValue(row.getCell(j));
//                    }
//                }
//            }
//            return data;
//        }
//    }

    // Get data from a specific cell
    public static String getCellData(String filePath, String sheetName, int rownum, int colnum) throws IOException {
        try (FileInputStream file = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(file)) {
            Sheet sheet = workbook.getSheet(sheetName);
            Row row = sheet.getRow(rownum);
            if (row == null) return "";

            Cell cell = row.getCell(colnum);
            if (cell == null) return "";

            DataFormatter formatter = new DataFormatter();
            return formatter.formatCellValue(cell);
        }
    }

    // Set data in a specific cell
    public static void setCellData(String filePath, String sheetName, int rownum, int colnum, String data) throws IOException {
        try (FileInputStream file = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(file)) {
            Sheet sheet = workbook.getSheet(sheetName);
            Row row = sheet.getRow(rownum);
            if (row == null) {
                row = sheet.createRow(rownum);
            }
            Cell cell = row.getCell(colnum);
            if (cell == null) {
                cell = row.createCell(colnum);
            }
            cell.setCellValue(data);

            try (FileOutputStream outFile = new FileOutputStream(filePath)) {
                workbook.write(outFile);
            }
        }
    }

    // Fill cell with green color
    public static void fillGreenColor(String filePath, String sheetName, int rownum, int colnum) throws IOException {
        applyCellColor(filePath, sheetName, rownum, colnum, IndexedColors.BRIGHT_GREEN.getIndex());
    }

    // Fill cell with red color
    public static void fillRedColor(String filePath, String sheetName, int rownum, int colnum) throws IOException {
        applyCellColor(filePath, sheetName, rownum, colnum, IndexedColors.RED.getIndex());
    }

    // Apply color to a cell
    private static void applyCellColor(String filePath, String sheetName, int rownum, int colnum, short colorIndex) throws IOException {
        try (FileInputStream file = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(file)) {
            Sheet sheet = workbook.getSheet(sheetName);
            Row row = sheet.getRow(rownum);
            if (row == null) {
                row = sheet.createRow(rownum);
            }
            Cell cell = row.getCell(colnum);
            if (cell == null) {
                cell = row.createCell(colnum);
            }

            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(colorIndex);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            cell.setCellStyle(style);

            try (FileOutputStream outFile = new FileOutputStream(filePath)) {
                workbook.write(outFile);
            }
        }
    }

    // Get row count from a sheet
    public static int getRowCount(String filePath, String sheetName) throws IOException {
        try (FileInputStream file = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(file)) {
            Sheet sheet = workbook.getSheet(sheetName);
            return sheet.getLastRowNum() + 1;
        }
    }
}
