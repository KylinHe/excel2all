package com.aliens.command.log;

/**
 * Created by hejialin on 2018/3/10.
 */
public class SystemLogger implements ILogger {

    @Override
    public void Info(String content) {
        System.out.println(content);
    }

    @Override
    public void Warning(String content) {
        System.out.println(content);
    }

    @Override
    public void Error(String content) {
        System.err.println(content);
    }
}
