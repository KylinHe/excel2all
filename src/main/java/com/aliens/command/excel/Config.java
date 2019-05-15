package com.aliens.command.excel;

import java.util.HashMap;
import java.util.Map;

public class Config {

    //表格名过滤
    private static String[] filter = null;

    //字段过滤
    private static String[] fieldFilter = null;

    //表格名别名
    private static Map<String, String> aliasMapping = new HashMap<String, String>();


    private Config() {

    }

    public static String getAlias(String name) {
        String alias = aliasMapping.get(name);
        if (alias == null) {
            alias = name;
        }
        return alias;
    }

    public static void setAlias(String name, String alias) {
        aliasMapping.put(name, alias);
    }

    public static void setFilter(String[] filter) {
        Config.filter = filter;
    }


    public static boolean isFilter(String name) {
        if (Config.filter == null) {
            return false;
        }

        for (String curr : Config.filter) {
            if (curr.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static void setFieldFilter(String[] filter) {
        Config.fieldFilter = filter;
    }


    public static boolean isFieldFilter(String name) {
        if (Config.fieldFilter == null) {
            return false;
        }

        for (String curr : Config.fieldFilter) {
            if (curr.equals(name)) {
                return true;
            }
        }
        return false;
    }
}
