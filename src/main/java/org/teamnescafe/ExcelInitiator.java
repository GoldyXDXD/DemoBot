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
    public int indexColumn = 1; //первый столбец пока не используется

    public StringBuilder stringTimetable = new StringBuilder();

    public String find(String group) throws IOException {
        File myFolder = new File("D:\\ExcelFiles");
        StringBuffer surnameBuffer = new StringBuffer(surname);
        surname = surnameBuffer.delete(0, 12).toString();
        if (searchTimetable(surname, myFolder) == null) {
            messageToSend = "Расписание не найдено";
        } else {
            messageToSend = printTimetable(searchTimetable(surname, myFolder).getName());
        }
        return messageToSend;
    }

    private File searchTimetable(String surname, File folder) throws IOException {
        File[] directoryFiles = folder.listFiles();
        for (File file: directoryFiles) {
            if (file.getName().startsWith(surname)) {
                return file;
            }
        }
        return null;
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

    private String printTimetable(String folder) throws IOException { // нужно будет исправить тройной цикл
        FileInputStream fileInputStream = new FileInputStream(folder);
        Workbook timetable = new XSSFWorkbook(fileInputStream);
        for (int i = 1; i < 37; i = i + 6) { // с понедельника по субботу (каждый день занимает 6 строк)
            for (int j = 0; j < 6; j++) {
                String[] dataFromTheTable = new String[4];
                for (int g = 0; g < dataFromTheTable.length; g++) {
                    dataFromTheTable[g] = getCellText(timetable.getSheetAt(0).getRow(j + i).getCell(indexColumn + g));
                    if (dataFromTheTable[g].isEmpty() || dataFromTheTable == null) {
                        dataFromTheTable[g] = "";
                    }
                    stringTimetable.append(dataFromTheTable[g]);
                }
            }
        }
        return stringTimetable.toString();
    }
}
