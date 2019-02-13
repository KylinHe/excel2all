package com.aliens.command.log;

/**
 * Created by hejialin on 2018/3/10.
 */
public interface ILogger {

    void Info(String content);

    void Warning(String content);

    void Error(String content);
}
