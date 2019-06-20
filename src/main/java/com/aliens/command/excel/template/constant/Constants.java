package com.aliens.command.excel.template.constant;

/**
 * Created by hejialin on 2018/3/13.
 */
public class Constants {

    public static final String TAG_TABLE_BEGIN = "<table>";
    public static final String TAG_ENUM_BEGIN = "<enum>";
    public static final String TAG_FIELD_BEGIN = "<field>";
    public static final String TAG_TABLE_END = "</table>";
//    public static final String TAG_ENUM_END = "</enum>";
//    public static final String TAG_FIELD_END = "</field>";

    public static final String PARAM_TABLE_ALIAS = "${table_alias}";  //表格别名     兵种表
    public static final String PARAM_TABLE_NAME = "${table_name}"; //表格名       army
    public static final String PARAM_TABLE_FIX_NAME = "${table_fixname}"; //表格名       Army
    public static final String PARAM_TABLE_UPPER_NAME = "${table_uppername}"; //表格名       ARMY
    public static final String PARAM_TABLE_DATA = "${table_data}"; //表格数据

    public static final String PARAM_FIELD_ALIAS = "${field_alias}"; //字段别名     字段1
    public static final String PARAM_FIELD_NAME = "${field_name}";   //字段名      field1
    public static final String PARAM_FIELD_TYPE = "${field_type}";   //字段类型      int float
    public static final String PARAM_FIELD_FIX_NAME = "${field_fixname}";   //字段名      Field1
    public static final String PARAM_FIELD_UPPER_NAME = "${field_uppername}";   //字段名      FIELD1

    public static final String PARAM_ENUM_NAME = "${enum_name}";  //枚举名
    public static final String PARAM_ENUM_VALUE = "${enum_value}"; //枚举值
    public static final String PARAM_ENUM_ALIAS = "${enum_alias}"; //枚举别名
    public static final String PARAM_ENUM_TYPE = "${enum_type}"; //枚举别名

    private Constants() {

    }
}
