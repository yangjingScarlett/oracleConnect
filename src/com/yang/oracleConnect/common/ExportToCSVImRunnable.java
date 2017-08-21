package com.yang.oracleConnect.common;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Yangjing
 */
public class ExportToCSVImRunnable implements Runnable {
    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
    private List<JSONObject> jsonList;
    private BufferedWriter bw;

    //当前数据量
    private int currItemCount;
    //数据缓存量
    private int dataCacheNum;

    public ExportToCSVImRunnable(BufferedWriter bw, int dataCacheNum) {
        this.bw = bw;
        this.dataCacheNum = dataCacheNum;
        jsonList = new ArrayList<>(dataCacheNum);
    }

    //判断缓存是否已满
    private boolean isCacheExpires() {
        return currItemCount >= dataCacheNum;
    }

    public boolean add(JSONObject jsonObject) {
        try {
            writeLock.lock();
            jsonList.add(jsonObject);
            currItemCount++;
            return isCacheExpires();
        } finally {
            writeLock.unlock();
        }
    }

    public void save(BufferedWriter bw) {
        try {
            writeLock.lock();

            //如果数据没有超出缓存，则返回
            if (!isCacheExpires()) {
                return;
            }

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            for (JSONObject json : jsonList) {
                //bw.write(json.getString("stu_id") + "," + json.getString("stu_name") + "," + json.getString("gender") + "\r\n");
                bw.write(String.valueOf(json.get("d_obj")) + "," + String.valueOf(json.get("order")) + ","
                        + String.valueOf(json.get("columns")) + "," + String.valueOf(json.get("types")) + "\r\n");
                currItemCount--;
            }

            stopWatch.stop();
            System.out.println(String.format("%s, 输出完成, 耗费时间：%s ms, 输出数据长度：%s", Thread.currentThread().getName(), stopWatch.getTime(), jsonList.size()));
            jsonList.clear();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }

    }

    public void flush(BufferedWriter bw) throws IOException {
        System.out.println(String.format("flush线程：%s, 需要保存数据的集合长度：%s", Thread.currentThread().getName(), jsonList.size()));

        for (JSONObject json : jsonList) {
            //bw.write(json.getString("stu_id") + "," + json.getString("stu_name") + "," + json.getString("gender") + "\r\n");
            bw.write(String.valueOf(json.get("d_obj")) + "," + String.valueOf(json.get("order")) + ","
                    + String.valueOf(json.get("columns")) + "," + String.valueOf(json.get("types")) + "\r\n");
        }

        System.out.println(String.format("flush线程：%s, 输出完成， 输出数据长度：%s", Thread.currentThread().getName(), jsonList.size()));
        jsonList.clear();
        closeWrite(bw);
    }

    private void closeWrite(BufferedWriter bw) throws IOException {
        bw.flush();
        bw.close();
    }

    @Override
    public void run() {
        save(bw);
    }
}
