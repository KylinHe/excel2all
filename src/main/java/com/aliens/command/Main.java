package com.aliens.command;

import com.aliens.command.excel.ExcelParser;
import com.aliens.command.excel.JsonConverter;
import com.aliens.command.excel.model.TableData;
import com.aliens.command.excel.template.ConverterManager;
import com.aliens.command.excel.template.dialect.GolandDialect;
import com.aliens.util.FileUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hejialin on 2018/3/13.
 */
public class Main {

    public static final String PARAM_NAME_DIALECT = "-d"; //输出语言方言

    public static final String PARAM_NAME_TEMPLATE = "-t"; //模板路径

    public static final String PARAM_NAME_FILTER = "-f"; //过滤模板名

    public static final String PARAM_NAME_INPUT = "-i"; //json输出目录

    public static final String PARAM_NAME_OUTPUT = "-o"; //输出路径

    public static void main(String[] args) {
        //dealUECommand(args);

        dealExcelCommand(args);
    }

    private static void dealExcelCommand(String[] args) {
        Map<String, String> params = new HashMap<String, String>();

        String name = null;
        String value = null;
        for (String arg : args) {
            if (name == null) {
                name = arg;
            } else if (value == null) {
                value = arg;
            }
            if (name != null && value != null) {
                params.put(name, value);
                name = null;
                value = null;
            }
        }

        System.out.println("args:" + params);


        String inputPath = params.get(PARAM_NAME_INPUT);
        if (inputPath == null || inputPath.isEmpty()) {
            System.err.println("invalid input path -i :" + inputPath);
            return;
        }

        String outPath = params.get(PARAM_NAME_OUTPUT);
        if (outPath == null || outPath.isEmpty()) {
            System.err.println("invalid outPath path -o :" + outPath);
            return;
        }

        String dialect = params.get(PARAM_NAME_DIALECT);
        if (dialect == null || dialect.isEmpty()) {
            System.err.println("invalid dialect -d :" + dialect);
            return;
        }

        File inputFile = new File(inputPath);
        if (!inputFile.exists()) {
            System.err.println("invalid input path -i :" + inputPath);
            return;
        }

        String filters = params.get(PARAM_NAME_FILTER);




        String templatePath = params.get(PARAM_NAME_TEMPLATE);

        ExcelParser parser = new ExcelParser();


        if (filters != null){
            String[] filter = filters.split(",");
            parser.setFilter(filter);
        }

        parser.parse(inputFile);

        if (dialect.equals("json")) {
            File output = new File(outPath);
            output.mkdirs();
            for (TableData data : parser.getData().values()) {
                new JsonConverter().convert(data, output.getAbsolutePath());
            }
        } else if (dialect.equals("go")) {
            ConverterManager template = new ConverterManager(templatePath);
            String content = template.convert(parser.getData().values(), GolandDialect.getInstance());
            FileUtil.instance.writeContent(outPath, content);
        } else if (dialect.equals("javascript")) {
            ConverterManager template = new ConverterManager(templatePath);
            String content = template.convert(parser.getData().values(), GolandDialect.getInstance());
            FileUtil.instance.writeContent(outPath, content);
        }
    }
}
