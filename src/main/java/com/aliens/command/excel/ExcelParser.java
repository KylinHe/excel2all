package com.aliens.command.excel;

import com.aliens.command.excel.model.TableData;
import com.aliens.command.log.ILogger;
import com.aliens.command.log.SystemLogger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hejialin on 2018/3/10.
 */
public class ExcelParser {

    private static final String EXCEL_TYPE_XLS = "xls";
    private static final String EXCEL_TYPE_XLSX = "xlsx";

    //解析的全局数据
    private Map<String, TableData> data = new HashMap<String, TableData>();

    private ILogger log = new SystemLogger();

    private String[] filter = null;

    public Map<String, TableData> getData() {
        return data;
    }


    public void setFilter(String[] filter) {
        this.filter = filter;
    }

    public boolean isFilter(String name) {
        if (this.filter == null) {
            return false;
        }

        for (String curr : this.filter) {
            if (curr.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public void parse(final File srcFile) {
        parse0(srcFile);
        Map<String, Map<String, Object>> mapping = getAllIDMapping();
        for (TableData tableData : this.data.values()) {
            updateRef(tableData, mapping);
        }

    }

    private int parse0(final File srcFile) {
        if(srcFile.isFile()){
            return parse1(srcFile, getPrefix(srcFile.getAbsolutePath()));
        }else{
            int successCount = 0 ;
            for (File f : srcFile.listFiles()) {
                if(f.isDirectory()) continue;
                successCount+=parse1(f, getPrefix(f.getAbsolutePath()));
            }
            return successCount;
        }
    }

    //get all id-name mapping
    private Map<String, Map<String, Object>> getAllIDMapping() {
        Map<String, Map<String, Object>> allMapping = new HashMap<String, Map<String, Object>>();
        for (Map.Entry<String, TableData> entry : data.entrySet()) {
            Map<String, Object> idMapping = entry.getValue().getIdMapping();
            if (idMapping != null && !idMapping.isEmpty()) {
                allMapping.put(entry.getKey(), idMapping);
            }
        }
        return allMapping;
    }

    private String getPrefix(String absolutePath){
        return absolutePath.substring(absolutePath.lastIndexOf('.')+1);
    }

    private int parse1(final File srcFile, final String fileType) {
        if (srcFile.getName().startsWith("~$")) {
            return 0;
        }
        if (this.isFilter(srcFile.getName())) {
            return 0;
        }
        Workbook workbook = null;
        try {
            switch (fileType) {
                case EXCEL_TYPE_XLS:
                    workbook = new HSSFWorkbook(new FileInputStream(srcFile));
                    break;
                case EXCEL_TYPE_XLSX:
                    workbook = new XSSFWorkbook(srcFile);
                    break;
                default:
            }
            if (workbook != null) {
                log.Info("parseTemplate excel file :" + srcFile.getAbsolutePath());
                readWorkBook(workbook);
            }
            return 1;
        } catch(Exception e){
            e.printStackTrace();
//            if(e instanceof ReadExcelException){
//                throw ((ReadExcelException) e).append(srcFile.getAlias());
//            }
        }finally{
            if(workbook != null){
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    private void readWorkBook(Workbook workbook) {
        SheetParser parser = null;
        TableData tableData = null;
        for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            String sheetName = sheet.getSheetName();
            if (data.containsKey(sheetName)) {
                log.Error("sheet" + sheet.getSheetName() + "already exists");
            } else if (sheetName.startsWith(SheetParser.FILTER_CHAR)) {
                log.Info("sheet" + sheet.getSheetName() + "is skip!");
            } else {
                tableData = new SheetParser().parse(sheet, workbook.getCreationHelper().createFormulaEvaluator());
                data.put(sheet.getSheetName(), tableData);
            }
        }
    }

    //update refer mapping
    public void updateRef(TableData data, Map<String, Map<String, Object>> mapping) {
        if (data == null || !data.haveRef()) {
            return;
        }

        for (Map.Entry<String, String> refer : data.getRefField().entrySet()) {
            String refTableName = refer.getValue();
            String fieldName = refer.getKey();
            Map<String, Object> referTableMapping = mapping.get(refTableName);
            if (referTableMapping == null || referTableMapping.isEmpty()) {
                log.Info("table " + data.getAlias() + " refer data not found :" + fieldName + " - " + refTableName);
                continue;
            }

            for (Map<String, Object> tableFields : data.getDataArray()) {
                Object refKey = tableFields.get(fieldName);
                if (refKey == null) {
                    continue;
                }
                if (refKey instanceof String[]) {
                    String[] keys = (String[])refKey;
                    List<Object> refValues = new ArrayList<Object>();
                    for (int i=0; i<keys.length; i++) {
                        Object refValue = referTableMapping.get(keys[i]);
                        if (refValue != null) {
                            refValues.add(refValue);
                        } else {
                            log.Info("table " + data.getAlias() + " refer value not found :" + keys[i]);
                            continue;
                        }
                    }
                    tableFields.put(fieldName, refValues.toArray());
                } else {
                    Object refValue = referTableMapping.get(refKey);
                    if (refValue != null) {
                        tableFields.put(fieldName, refValue);
                    } else {
                        log.Info("table " + data.getAlias() + " refer value not found :" + refKey);
                        continue;
                    }
                }


            }
        }
    }



    public static void main(String[] args) throws Exception {
        ExcelParser parser = new ExcelParser();
        parser.parse(new File("/Users/hejialin/Downloads/testconvert"));
        File output = new File("/Users/hejialin/Downloads/testconvert/output");
        output.mkdirs();
        for (TableData data : parser.getData().values()) {
            new JsonConverter().convert(data, output.getAbsolutePath());
        }

    }
}
