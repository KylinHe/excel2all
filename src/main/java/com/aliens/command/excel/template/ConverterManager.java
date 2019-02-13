package com.aliens.command.excel.template;

import com.aliens.command.excel.ExcelParser;
import com.aliens.command.excel.model.TableData;
import com.aliens.command.excel.template.constant.Constants;
import com.aliens.command.excel.template.dialect.Dialect;
import com.aliens.command.excel.template.dialect.GolandDialect;
import com.aliens.command.excel.template.model.Template;
import com.aliens.util.FileUtil;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hejialin on 2018/3/12.
 */
public class ConverterManager {

    private String templatePath;

    //private String templateContent;

    private List<Template> templates;

    private Map<String, Converter> converters;

    public ConverterManager(String templatePath) {
        this.templatePath = templatePath;
        init();
    }

    public void init() {
        String content = FileUtil.instance.readContent(templatePath);
        this.templates = TemplateParser.parse(content, Constants.TAG_TABLE_BEGIN, Constants.TAG_TABLE_END);
        converters = new HashMap<String, Converter>();
        converters.put(Constants.TAG_TABLE_BEGIN, new TableConverter());
        converters.put(Constants.TAG_ENUM_BEGIN, new EnumConverter());
        converters.put(Constants.TAG_FIELD_BEGIN, new FieldConverter());
    }

    public String convert(Collection<TableData> tableData, Dialect dialect) {
        if (this.templates == null || this.templates.isEmpty()) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        for (Template template : templates) {
            Converter converter = converters.get(template.getTag());
            if (converter != null) {
                result.append(converter.convert(tableData, dialect, template));
            }
        }

        return result.toString();
    }


    public static void main(String[] args) {

        ExcelParser parser = new ExcelParser();
        parser.parse(new File("/Users/hejialin/Downloads/testconvert"));

        ConverterManager template = new ConverterManager("/Users/hejialin/Downloads/testconvert/js.template");

        String content = template.convert(parser.getData().values(), GolandDialect.getInstance());

        FileUtil.instance.writeContent("/Users/hejialin/Downloads/testconvert/js.out", content);
    }
}
