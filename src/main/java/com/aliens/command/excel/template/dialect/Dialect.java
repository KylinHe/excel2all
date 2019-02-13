package com.aliens.command.excel.template.dialect;

import com.aliens.command.excel.model.FieldType;

/**
 * Created by hejialin on 2018/3/12.
 */
public interface Dialect {

    //获取字段类型
    String getType(FieldType fieldType);
}
