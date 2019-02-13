package com.aliens.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejialin on 2018/3/12.
 */
public enum CharacterUtil {

    instance;

    public String transferCamelCasing(String content) {
        char[] camelContent = content.toCharArray();

        StringBuilder result = new StringBuilder();

        boolean lastIS_ = false;
        for (int i=0; i<camelContent.length; i++) {
            char currChar = camelContent[i];
            if (i == 0) {
                result.append(String.valueOf(currChar).toUpperCase());
                continue;
            }


            if (currChar == '_') {
                lastIS_ = true;
                continue;
            }
            if (lastIS_) {
                result.append(String.valueOf(currChar).toUpperCase());
                lastIS_ = false;
            } else {
                result.append(currChar);
            }
        }
        return result.toString();
    }

    public String[] splitText(String fieldText) {
        char[] chars = fieldText.toCharArray();
        List<String> trimContents = new ArrayList<String>();
        StringBuilder currStr = new StringBuilder();
        for (char currChar : chars) {
            if (currChar == ' ' || currChar == '\n' || currChar == '\r' || currChar == '\t') {
                if (currStr.length() != 0) {
                    trimContents.add(currStr.toString());
                    currStr = new StringBuilder();
                }
                continue;
            }
            currStr.append(currChar);
        }
        if (currStr.length() != 0) {
            trimContents.add(currStr.toString());
        }
        return trimContents.toArray(new String[trimContents.size()]);
    }

}
