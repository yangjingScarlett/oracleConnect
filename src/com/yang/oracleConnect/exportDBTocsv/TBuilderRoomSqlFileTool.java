package com.yang.oracleConnect.exportDBTocsv;

import com.yang.oracleConnect.common.CallBack;
import com.yang.oracleConnect.common.StopWatch;
import com.yang.oracleConnect.common.TaskWithResultImCallable;
import com.yang.oracleConnect.common.WriteDataHandle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Yangjing
 */
public class TBuilderRoomSqlFileTool {
    final static int BSIZE = 1024 * 1024;
    private final static int DATACACHENUM = 10;
    private static int currThreadCount = 0;
    private static int maxThreadCount = 5;
    private static File dataFile = new File("f://dataFile.csv");

    private static BufferedWriter initDataWrite(File dataFile) throws Exception {
        if (!dataFile.exists()) {
            if (!dataFile.createNewFile()) {
                System.err.println("创建文件失败，已存在：" + dataFile.getAbsolutePath());
            }
        }
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dataFile, true), "UTF-8"));
    }

    //读一行数据
    private static void loadDB(CallBack<Void> callBack) throws Exception {
        Class.forName("oracle.jdbc.driver.OracleDriver");

        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:orcl", "scarlett", "Yangjing95");
        String sql = "select * from student";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        int num = 0;
        while (rs.next()) {
            String stu_id = rs.getString("STU_ID");
            String stu_name = rs.getString("STU_NAME");
            String gender = rs.getString("GENDER");
            String birthdate = rs.getString("BIRTHDATE");
            String enterdate = rs.getString("ENTERDATE");
            String address = rs.getString("ADDRESS");
            String subject = rs.getString("SUBJECT");
            String classno = rs.getString("CLASSNO");

            String line = stu_id + stu_name + gender + birthdate + enterdate + address + subject + classno;
            num++;
            callBack.call(num, line);
        }
    }

    private static void writeLog() {
        System.out.println("录入错误的数据：:0");
    }

    public static void main(String[] args) throws Exception {
        final ExecutorService threadPool = Executors.newFixedThreadPool(maxThreadCount);
        final List<Future<String>> threadResultList = new ArrayList<>();
        final BufferedWriter bw = initDataWrite(dataFile);
        final WriteDataHandle writeDataFile = new WriteDataHandle(DATACACHENUM);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        loadDB(new CallBack<Void>() {

            @Override
            public Void call(int num, String data) {
                try {
                    // 添加数据，如果超出了缓存数据，则 开始写入文件系统
                    if (writeDataFile.add(data)) {
                        currThreadCount++;//0
                        // 如果提交的线程过多，则取回之后再提交.
                        if (currThreadCount >= maxThreadCount) {
                            for (Future<String> ignored : threadResultList) {
                                currThreadCount--;
                            }
                            threadResultList.clear(); // 清空
                            currThreadCount = threadResultList.size();
                        }
                        Future<String> future = threadPool.submit(new TaskWithResultImCallable(writeDataFile, bw));
                        threadResultList.add(future);
                    }

                } catch (Exception e) {
                    writeLog();
                }
                return null;

            }
        });
        writeDataFile.flush(bw);
        threadPool.shutdown();
        stopWatch.stop();
        System.out.println(String.format("任务完成时间:%s ms", stopWatch.getTime()));
    }
}

