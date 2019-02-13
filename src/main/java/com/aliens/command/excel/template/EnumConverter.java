package com.aliens.command.excel.template;

import com.aliens.command.excel.model.TableData;
import com.aliens.command.excel.model.TableEnum;
import com.aliens.command.excel.model.TableField;
import com.aliens.command.excel.template.constant.Constants;
import com.aliens.command.excel.template.dialect.Dialect;
import com.aliens.command.excel.template.model.Template;

import java.util.Collection;
import java.util.Map;

/**
 * Created by hejialin on 2018/3/13.
 */
public class EnumConverter implements Converter {

    @Override
    public String convert(Collection<TableData> tableData, Dialect dialect, Template template) {
        if (template == null) {
            return null;
        }
        StringBuilder content = new StringBuilder();
        content.append(template.getHeader());
        String body = template.getBody();
        for (TableData data : tableData) {
            if (body == null || body.isEmpty()) {
               continue;
            }
            for (TableField field : data.getFieldInfo()) {
                if (!field.isEnum()) {
                    continue;
                }
                String currTablePrefix = TableConverter.replaceTableContent(data, template.getBodyPrefix());
                currTablePrefix = currTablePrefix.replace(Constants.PARAM_FIELD_ALIAS, field.getAlias());
                content.append(currTablePrefix);

                for (Map.Entry<String, TableEnum> enumInfo : field.getEnums().entrySet()) {
                    String currEnumContent = body.replace(Constants.PARAM_ENUM_ALIAS, enumInfo.getKey());
                    currEnumContent = currEnumContent.replace(Constants.PARAM_ENUM_NAME, enumInfo.getValue().getName());
                    currEnumContent = currEnumContent.replace(Constants.PARAM_ENUM_TYPE, dialect.getType(field.getFieldType()));
                    currEnumContent = currEnumContent.replace(Constants.PARAM_ENUM_VALUE, String.valueOf(field.getEnum(enumInfo.getKey())));
                    content.append(currEnumContent);
                }
            }
            content.append(template.getBodySuffix());
        }
        content.append(template.getTail());
        return content.toString();
    }

}
