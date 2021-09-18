package stat.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;

public class ExcelFileWriter {
    private String filePath;
    private XSSFWorkbook workbook;
    private XSSFSheet workSheet;
    private XSSFRow row;
    private XSSFCell cell;
    private int rowNum;



    public ExcelFileWriter(String filePath) {
        rowNum = 0;
        this.filePath = filePath;
        this.workbook = new XSSFWorkbook();
        this.workSheet = workbook.createSheet();
        addFirstRow();
    }

    private void addFirstRow() {
        this.row = workSheet.createRow(rowNum);
        Cell cell = row.createCell(0);
        cell.setCellValue("SE");

        cell = row.createCell(1);
        cell.setCellValue("SP");

        cell = row.createCell(2);
        cell.setCellValue("AC");

        cell = row.createCell(3);
        cell.setCellValue("Misidentified");

        rowNum++;
    }

    public void addToWriter(String[] objArr) {
        this.row = workSheet.createRow(rowNum);
        int cellid = 0;
        for (String obj : objArr) {
            Cell cell = row.createCell(cellid++);
            cell.setCellValue(obj);
        }
        rowNum++;
    }

    public void writeFile() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(this.filePath));
            workbook.write(fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
