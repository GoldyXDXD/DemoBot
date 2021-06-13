package org.teamnescafe;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ExcelInitiator {
    public String messageToSend = "";
//    public int indexRowGroup;
//    public int indexCellGroup;

    //public StringBuilder = new StringBuilder();

    public String find(String group) throws IOException {
        File myFolder = new File("D:\\ExcelFiles");
        File[] files = myFolder.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (searchGroup(group, files[i].toString())) {
//                messageToSend = printExcel(files[i].toString());
                break;
            }
        }
        if (messageToSend.equals("")) {
            messageToSend = "Расписание не найдено";
        }
        return messageToSend;
    }

    public boolean searchGroup(String group, String folder) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(folder);
        Workbook wb = new XSSFWorkbook(fileInputStream);
//        for (Row row : wb.getSheetAt(0)) {
//            for (Cell cell : row) {
//                Cell r = cell;
//                if (getCellText(cell).equals(group)){
//                    indexRowGroup = r.getRowIndex();
//                    indexCellGroup = r.getColumnIndex();
//                    test = 1;
//                    break;
//                }
//            }
//        }
//        return test;
//    }
    }
    
    public String getCellText (Cell cell){
        String result = "";
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                result = cell.getRichStringCellValue().getString();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    result = cell.getDateCellValue().toString();
                } else {
                    result = Double.toString(cell.getNumericCellValue());
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                result = Boolean.toString(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA:
                result = cell.getCellFormula().toString();
                break;
                default:
                    break;
        }
        return result;
    }
