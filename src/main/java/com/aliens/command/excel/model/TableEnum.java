package com.aliens.command.excel.model;

/**
 * Created by hejialin on 2018/3/14.
 */
public class TableEnum {

    private String name;

    private String alias;

    private int value;

    public TableEnum(String name, String alias, int value) {
        this.name = name;
        this.alias = alias;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TableEnum tableEnum = (TableEnum) o;

        if (value != tableEnum.value) return false;
        if (name != null ? !name.equals(tableEnum.name) : tableEnum.name != null) return false;
        return alias != null ? alias.equals(tableEnum.alias) : tableEnum.alias == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (alias != null ? alias.hashCode() : 0);
        result = 31 * result + value;
        return result;
    }
}
