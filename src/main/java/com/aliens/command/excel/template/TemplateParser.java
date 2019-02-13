package com.aliens.command.excel.template;

import com.aliens.command.excel.template.constant.Constants;
import com.aliens.command.excel.template.model.Template;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hejialin on 2018/3/12.
 */
public class TemplateParser {

    public static final Pattern ENUM_MATCH_REG = Pattern.compile("([\\s\\S]*)<enum>([\\s\\S]*)</enum>([\\s\\S]*)");
    public static final Pattern FIELD_MATCH_REG = Pattern.compile("([\\s\\S]*)<field>([\\s\\S]*)</field>([\\s\\S]*)");


    public static List<Template> parse(String content, String startTag, String endTag) {
        List<Template> templates = new ArrayList<Template>();

        char[] contents = content.toCharArray();
        StringBuilder buff = new StringBuilder();
        Template current = new Template();
        //current parse lex
        StringBuilder lex = new StringBuilder();
        for (char currChar : contents) {
            if (currChar == '>') {
                System.out.println();
            }
            if (currChar == ' ' || currChar == '\r' || currChar == '\n' || currChar == '\t' || currChar == ' ') {
                if (lex.length() > 0) {
                    buff.append(lex);
                    lex = new StringBuilder();
                }
                buff.append(currChar);
                continue;
            }

            lex.append(currChar);

            if (lex.toString().equals(startTag)) {
                current.add(buff.toString());
                buff = new StringBuilder();
                lex = new StringBuilder();

            } else if (lex.toString().equals(endTag)) {
                current.add(buff.toString());
                if (!parseTemplate(current, FIELD_MATCH_REG, Constants.TAG_FIELD_BEGIN)) {
                    if (!parseTemplate(current, ENUM_MATCH_REG, Constants.TAG_ENUM_BEGIN)) {
                        current.setTag(startTag);
                    }
                }

                templates.add(current);

                buff = new StringBuilder();
                lex = new StringBuilder();
                current = new Template();
            }
        }

        if (!templates.isEmpty()) {
            if (lex.length() > 0) {
                buff.append(lex);
            }
            templates.get(templates.size() - 1).add(buff.toString());
        }

        return templates;
    }


    public static boolean parseTemplate(Template template, Pattern pattern, String tag) {
        String tableBody = template.getBody();
        Matcher bodyMatcher = pattern.matcher(tableBody);

        if (bodyMatcher.find()) {
            template.setTag(tag);
            template.setBodyPrefix(bodyMatcher.group(1));
            template.setBody(bodyMatcher.group(2));
            template.setBodySuffix(bodyMatcher.group(3));
            return true;
        }
        template.setBodyPrefix("");
        template.setBodySuffix("");
        return false;
    }

}
