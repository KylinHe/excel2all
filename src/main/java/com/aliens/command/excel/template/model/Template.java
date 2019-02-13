package com.aliens.command.excel.template.model;

/**
 * Created by hejialin on 2018/3/12.
 */
public class Template {

    //ENUM FIELD
    private String tag;

    private String header;

    private String tail;

    private String bodyPrefix;

    private String body;

    private String bodySuffix;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getTail() {
        if (tail == null) {
            return "";
        }
        return tail;
    }

    public void setTail(String tail) {
        this.tail = tail;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getBodyPrefix() {
        return bodyPrefix;
    }

    public void setBodyPrefix(String bodyPrefix) {
        this.bodyPrefix = bodyPrefix;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBodySuffix() {
        return bodySuffix;
    }

    public void setBodySuffix(String bodySuffix) {
        this.bodySuffix = bodySuffix;
    }

    public void add(String content) {
        if (this.header == null) {
            this.header = content;
        } else if (this.body == null) {
            this.body = content;
        } else if (this.tail == null) {
            this.tail = content;
        }

    }


    @Override
    public String toString() {
        return "Template{" +
                "header='" + header + '\'' +
                ", tail='" + tail + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
