package com.aliens.command.excel.model;

/**
 * Created by hejialin on 2018/3/10.
 */
public enum FieldType {

    //-------base type--------
    STRING("string"),
    FLOAT("float"),
    BOOL("bool"),
    INT("int"),

    ENUM("enum"),
    ID("id"),
    NAME("name"),
    ARRAY("array"),  //[int]
    JSON("json"),  // {field:int,abc:string}

    //refer other table
    REFER("refer");

    private String type;

    private FieldType(String type) {
        this.type = type;
    }

    public static FieldType getFieldType(String type) {
        for (FieldType fieldType : FieldType.values()) {
            if (fieldType.type.equals(type)) {
                return fieldType;
            }
        }
        return null;
    }
}
