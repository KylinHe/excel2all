package com.aliens.command.excel;

import com.alibaba.fastjson.JSONObject;
import com.aliens.command.excel.model.FieldType;
import com.aliens.command.excel.model.TableData;
import com.aliens.command.excel.model.TableEnum;
import com.aliens.command.excel.model.TableField;
import com.aliens.command.log.ILogger;
import com.aliens.command.log.SystemLogger;
import com.aliens.util.CharacterUtil;
import org.apache.poi.ss.usermodel.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hejialin on 2018/3/9.
 */
public class SheetParser {

    public static final String TABLE_NAME_KEY = "C";

    public static final String ENUM_SPLIT_CHAR = ":";

    public static final String FILTER_CHAR = "#";

    public static final String ARRAY_SPLIT = ",";

    public static final int[] lineNo = new int[]{0,1,2};

    private ILogger log = new SystemLogger();

    private FormulaEvaluator evaluator;


    public TableData parse(Sheet sheet, FormulaEvaluator evaluator) {
        TableData data = new TableData(Config.getAlias(sheet.getSheetName()));
        this.evaluator = evaluator;
        int fieldNameNo = sheet.getFirstRowNum() + lineNo[0];
        int fieldTypeNo = sheet.getFirstRowNum() + lineNo[1];
        int descRowNo = sheet.getFirstRowNum() + lineNo[2];

        loadFieldName(data, sheet.getRow(fieldNameNo));
        loadFieldType(data, sheet.getRow(fieldTypeNo));
        loadFieldDesc(data, sheet.getRow(descRowNo));

        for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if(row == null){
                continue;
            }
            if (rowIndex != fieldNameNo && rowIndex != fieldTypeNo && rowIndex != descRowNo){
                Cell cell = row.getCell(0);
                if (cell == null) {
                    continue;
                }
                String cellContent = getStringValue(cell);
                if (!cellContent.startsWith(FILTER_CHAR)) {
                    loadFieldData(data, row);
                }
            }
        }

        data.mergeField();
        return data;
    }

    private void loadFieldDesc(TableData data, Row dataRow) {
        TableField field = null;
        List<TableField> fieldInfo = data.getFieldInfo();
        for (int i = 0; i < fieldInfo.size(); i++) {
            field = fieldInfo.get(i);
            Cell cell = dataRow.getCell(field.getIndex());
            if (cell != null) {
                field.setAlias(cell.getStringCellValue());
            }
        }
    }

    private void loadFieldData(TableData data, Row dataRow) {
        TableField field = null;
        List<TableField> fieldInfo = data.getFieldInfo();
        Map<String, Object> fieldData = new LinkedHashMap<String, Object>();
        Object id = null;
        String name = null;
        for (int i = 0; i < fieldInfo.size(); i++) {
            field = fieldInfo.get(i);
            Cell cell = dataRow.getCell(field.getIndex());
            if (cell == null) {
                if (field.getFieldType() == FieldType.ID) {
                    return;
                }
                break;
            }
            String key = getStringValue(cell).trim();
            Object value = getFieldValue(field.getFieldType(), field.getSubType(), key, field);

            if (field.getFieldType() == FieldType.ID) {
                id = value;
            } else if (field.getFieldType() == FieldType.NAME) {
                name = String.valueOf(value);
            }

            fieldData.put(field.getName(), value);
        }

        if (id != null && name != null) {
            data.addTableIDMapping(id, name);
        }
        data.addData(fieldData);
    }

    private Object getFieldValue(FieldType fieldType, FieldType subType, String content, TableField field) {
        switch(fieldType) {
            case STRING:
                return content;
            case BOOL:
                return Boolean.parseBoolean(content);
            case INT:
                try {
                    return (int)Float.parseFloat(content);
                } catch (NumberFormatException e) {
                    return 0;
                }
            case FLOAT:
                try {
                    return Float.parseFloat(content);
                } catch (NumberFormatException e) {
                    return 0.0f;
                }
            case ENUM:
                Integer enumValue = field.getEnum(content);
                if (enumValue == null) {
                    log.Error("unexpect field " + field.getName() + " enum " + content);
                    enumValue = Integer.valueOf(0);
                }
                return enumValue;
            case ID:
                try {
                    return (int)Float.parseFloat(content);
                } catch (NumberFormatException e) {
                    return content;
                }
            case ARRAY:
                String[] arrayInfo = content.split(ARRAY_SPLIT);
                if (subType != null && subType != FieldType.REFER) {
                    List<Object> result = new ArrayList<Object>();
                    for (String info : arrayInfo) {
                        result.add(getFieldValue(subType, null, info.trim(), field));
                    }
                    return result.toArray();
                } else {
                    return arrayInfo;
                }
            case NAME:
                return content;
            case JSON:
                return JSONObject.parse(content);
            default:
                return content;
        }
    }

    private String getStringValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        CellType type = cell.getCellType();
        if (type == CellType.NUMERIC) {

            return String.valueOf(cell.getNumericCellValue());
        } else if (type == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (type == CellType.FORMULA) {
            CellValue cellValue = evaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case BOOLEAN:
                    return String.valueOf(cellValue.getBooleanValue());
                case NUMERIC:
                    return String.valueOf(cellValue.getNumberValue());
                case STRING:
                    return cellValue.getStringValue();
                default:
                    return "";
            }
        }
        return cell.getStringCellValue();
    }


    //load field meta info
    private void loadFieldName(TableData data, Row row) {
        List<TableField> fields = new ArrayList<TableField>();
        TableField field = null;
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell == null) {
                break;
            }
            String fieldName = getStringValue(cell).trim();
            if (fieldName.equals("")) {
                break;
            }
            if (Config.isFieldFilter(fieldName)) {
                continue;
            }
            field = new TableField(fieldName, i);
            fields.add(field);
        }
        data.setFieldInfo(fields);
    }

    //load field meta info
    private void loadFieldType(TableData data, Row row) {
        TableField field = null;
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            field = data.getField(i);
            if (field == null) {
                break;
            }

            Cell cell = row.getCell(i);
            if (cell == null) {
                break;
            }

            String parseField = getStringValue(cell).trim();
            if(parseField.isEmpty()) {
                break;
            }

            String[] fieldInfo = CharacterUtil.instance.splitText(parseField);
            if (fieldInfo == null || fieldInfo.length == 0) {
                log.Warning("invalid field format : " + parseField + " at column " + i);
                break;
            }

            //parseTemplate field type
            String fieldInfoText = fieldInfo[0];
            FieldType fieldType = getFieldType(fieldInfoText);
            FieldType subFieldType = null;
            if (fieldType == null) {
                log.Warning("un expect field type : " + fieldInfoText + " at column " + i);
            }

            if (fieldType == FieldType.ARRAY) {
                fieldInfoText = fieldInfoText.substring(1, fieldInfoText.length() - 1);
                subFieldType = getFieldType(fieldInfoText);
            }

            field.setFieldType(fieldType);
            field.setSubType(subFieldType);
            updateData(fieldType, data, field, fieldInfoText, fieldInfo);
            updateData(subFieldType, data, field, fieldInfoText, fieldInfo);
        }


    }

    private FieldType getFieldType(String fieldInfoText) {
        FieldType fieldType = null;
        //FieldType subFieldType = null;
        if (fieldInfoText.startsWith("$")) {
            fieldType = FieldType.REFER;
        } else if (fieldInfoText.startsWith("[") && fieldInfoText.endsWith("]")){
            fieldType = FieldType.ARRAY;
        } else {
            fieldType = FieldType.getFieldType(fieldInfoText);
        }
        return fieldType;

    }

    private void updateData( FieldType fieldType, TableData data, TableField field, String fieldInfoText, String[] fieldInfo) {
        if (fieldType == FieldType.REFER) {
            field.setRef(fieldInfoText.substring(1));
            data.addRefField(field.getName(), field.getRef());
        } else if (fieldType == FieldType.ENUM) {
            Map<String, TableEnum> enumMapping = readEnum(fieldInfo);
            field.setEnums(enumMapping);
        } else if (fieldType == FieldType.ID) {
            Map<String, TableEnum> enumMapping = readEnum(fieldInfo);
            if (enumMapping != null) {
                String tableName = enumMapping.get(TABLE_NAME_KEY).getName();
                if (tableName != null) {
                    data.setName(tableName);
                }
            }
        }

    }
    private Map<String, TableEnum> readEnum(String[] fieldInfo) {
        if (fieldInfo.length <= 1) {
            return null;
        }
        Map<String, TableEnum> enumMapping = new LinkedHashMap<String, TableEnum>();
        int index = 1;

        for (int j = 1; j<fieldInfo.length; j++) {
            String enumInfo = fieldInfo[j];
            String[] enums = enumInfo.trim().split(ENUM_SPLIT_CHAR);
            if (enums.length < 2) {
                continue;
            }
            if (enums.length == 3) {
                try {
                    index = Integer.parseInt(enums[2].trim());
                } catch(NumberFormatException e) {

                }
            }
            TableEnum tableEnum = new TableEnum(enums[1].trim(), enums[0].trim(), index);
            enumMapping.put(tableEnum.getAlias(), tableEnum);
            index ++;
        }
        return enumMapping;
    }

}