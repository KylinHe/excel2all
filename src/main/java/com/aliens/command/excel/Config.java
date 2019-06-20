package com.aliens.command.excel;

import java.util.HashMap;
import java.util.Map;

public class Config {

    //表格名过滤
    private static String[] exclude = null;

    //表格名过滤
    private static String[] include = null;

    //字段过滤
    private static String[] fieldFilter = null;

    private static Map<String, String> fieldTypeAliasMapping = new HashMap<String, String>();

    //表格名别名
    private static Map<String, String> aliasMapping = new HashMap<String, String>();


    private Config() {

    }

    public static String getFiledTypeAlias(String name) {
        return fieldTypeAliasMapping.get(name);
    }

    public static void setFiledTypeAlias(String fieldName, String fieldTypeAlias) {
        fieldTypeAliasMapping.put(fieldName, fieldTypeAlias);
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

    public static void setExclude(String[] exclude) {
        Config.exclude = exclude;
    }

    public static void setInclude(String[] include) {
        Config.include = include;
    }

    public static boolean isFilter(String name) {
        boolean haveInclude = Config.include != null && Config.include.length > 0;
        if (haveInclude) {
            for (String curr : Config.include) {
                if (curr.equals(name)) {
                    return false;
                }
            }
        }
        if (Config.exclude != null) {
            for (String curr : Config.exclude) {
                if (curr.equals(name)) {
                    return true;
                }
            }
        }
        //有包含条件、默认需要过滤
        return haveInclude;
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
