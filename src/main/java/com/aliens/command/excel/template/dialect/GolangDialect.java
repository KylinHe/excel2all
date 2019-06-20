package com.aliens.command.excel.template.dialect;

import com.aliens.command.excel.model.FieldType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hejialin on 2018/3/12.
 */
public class GolangDialect implements Dialect {

    private static GolangDialect instance;

    public static Map<FieldType, String> fieldTypemapping = new HashMap<FieldType, String>();

    private GolangDialect() {

    }

    public static synchronized GolangDialect getInstance() {
        if (instance == null) {
            instance = new GolangDialect();
        }
        return instance;
    }

//    STRING("string"),
//    FLOAT("float"),
//    BOOL("bool"),
//    INT("int"),
//
//    ENUM("enum"),
//    ID("id"),
//    NAME("name"),
//    ARRAY("json"),  //[int]
//    Object("object"),  // {field:int,abc:string}
//
//    //refer other table
//    REFER("refer");


    static {
        fieldTypemapping.put(FieldType.STRING, "string");
        fieldTypemapping.put(FieldType.FLOAT, "float32");
        fieldTypemapping.put(FieldType.BOOL, "bool");
        fieldTypemapping.put(FieldType.INT, "int32");

        fieldTypemapping.put(FieldType.ENUM, "int32");
        fieldTypemapping.put(FieldType.ID, "int32");
        fieldTypemapping.put(FieldType.NAME, "string");

        fieldTypemapping.put(FieldType.REFER, "int32");
        fieldTypemapping.put(FieldType.JSON, "string");
    }

    @Override
    public String getType(FieldType fieldType) {
        String result = fieldTypemapping.get(fieldType);
        if (result == null) {
            result = "int32";
        }
        return result;
    }
}
