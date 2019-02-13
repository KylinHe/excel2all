package com.aliens.command.excel.template;

import com.aliens.command.excel.model.TableData;
import com.aliens.command.excel.template.constant.Constants;
import com.aliens.command.excel.template.dialect.Dialect;
import com.aliens.command.excel.template.model.Template;

import java.util.Collection;

/**
 * Created by hejialin on 2018/3/13.
 */
public class TableConverter implements Converter {

    @Override
    public String convert(Collection<TableData> tableData, Dialect dialect, Template template) {
        if (template == null) {
            return null;
        }
        StringBuilder content = new StringBuilder();

        content.append(template.getHeader());

        String body = template.getBody();
        for (TableData currData : tableData) {
            String tablePrefix = replaceTableContent(currData, body);
            content.append(tablePrefix);
        }
        content.append(template.getTail());
        return content.toString();
    }


    public static String replaceTableContent(TableData data, String currTablePrefix) {
        currTablePrefix = currTablePrefix.replace(Constants.PARAM_TABLE_ALIAS, data.getAlias());
        currTablePrefix = currTablePrefix.replace(Constants.PARAM_TABLE_NAME, data.getName());
        currTablePrefix = currTablePrefix.replace(Constants.PARAM_TABLE_FIX_NAME, data.getFixName());
        currTablePrefix = currTablePrefix.replace(Constants.PARAM_TABLE_UPPER_NAME, data.getUpperName());
        currTablePrefix = currTablePrefix.replace(Constants.PARAM_TABLE_DATA, data.getJsonData());
        return currTablePrefix;
    }
}
