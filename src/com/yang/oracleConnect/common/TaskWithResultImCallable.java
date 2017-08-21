package com.yang.oracleConnect.common;

import java.io.BufferedWriter;
import java.util.concurrent.Callable;

/**
 * @author Yangjing
 */
public class TaskWithResultImCallable implements Callable<String> {
    private WriteDataHandle handle;

    private BufferedWriter bufferedWriter;

    public TaskWithResultImCallable(WriteDataHandle handle, BufferedWriter bufferedWriter) {
        this.handle = handle;
        this.bufferedWriter = bufferedWriter;
    }

    @Override
    public String call() throws Exception {
        String fileName = Thread.currentThread().getName();

        handle.save(bufferedWriter);

        return fileName;
    }

}
