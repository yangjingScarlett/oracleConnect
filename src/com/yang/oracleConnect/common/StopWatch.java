package com.yang.oracleConnect.common;

/**
 * @author Yangjing
 */
public class StopWatch {
    private long begin;
    private long end;

    public void start() {
        begin = System.currentTimeMillis();
    }

    public void stop() {
        end = System.currentTimeMillis();
    }

    public long getTime() {
        return end - begin;
    }

}
