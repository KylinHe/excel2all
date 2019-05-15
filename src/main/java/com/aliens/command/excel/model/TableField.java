package com.aliens.command.excel.model;

import com.aliens.util.CharacterUtil;

import java.util.Map;

/**
 * Created by hejialin on 2018/3/10.
 */
public class TableField {

    //the field name
    private String name;

    //字段名转驼峰
    private String fixName;

    //字段名转大写
    private String upperName;

    private String alias;

    //the field Type
    private FieldType fieldType;

    //array sub Type
    private FieldType subType;

    //refer other sheet
    private String ref;

    //字段的索引位置
    private int index = 0;

    //the field is enum;
    private Map<String, TableEnum> enums;

    public TableField(String name, int index) {
        this.name = name;
        this.alias = name;
        this.index = index;
    }

    public TableField(String name, FieldType fieldType) {
        this.name = name;
        this.alias = name;
        this.fieldType = fieldType;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFixName() {
        if (this.fixName == null) {
            this.fixName = CharacterUtil.instance.transferCamelCasing(this.name);
        }
        return this.fixName;
    }

    public String getUpperName() {
        if (this.upperName == null) {
            this.upperName = this.name.toUpperCase();
        }
        return this.upperName;
    }

    public FieldType getSubType() {
        return subType;
    }

    public void setSubType(FieldType subType) {
        this.subType = subType;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public Map<String, TableEnum> getEnums() {
        return enums;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public Integer getEnum(String enumName) {
        if (enums == null) {
            return 0;
        }
        TableEnum enumValue = enums.get(enumName);
        if (enumValue == null) {
            return 0;
        }
        return  enumValue.getValue();
    }

    public void setEnums(Map<String, TableEnum> enums) {
        this.enums = enums;
    }

    public boolean isEnum() {
        return enums != null && !enums.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TableField field = (TableField) o;

        return name != null ? name.equals(field.name) : field.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "TableField{" +
                "name='" + name + '\'' +
                ", fieldType=" + fieldType +
                ", ref='" + ref + '\'' +
                ", enums=" + enums +
                '}';
    }
}
