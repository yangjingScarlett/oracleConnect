package com.yang.oracleConnect.service.impl;

import com.yang.oracleConnect.common.ExportToCSVImRunnable;
import com.yang.oracleConnect.common.StopWatch;
import com.yang.oracleConnect.dao.AccessDao;
import com.yang.oracleConnect.dao.AccessOraclePagingDao;
import com.yang.oracleConnect.entity.Access;
import com.yang.oracleConnect.service.AccessMultiThreadService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Yangjing
 * 实现导出数据库数据到CSV文件，用BufferedReader来实现
 */
@Service
@Transactional(transactionManager = "transactionManager")
public class AccessMultiThreadServiceImpl implements AccessMultiThreadService {
    @Autowired
    AccessDao accessDao;

    @Autowired
    AccessOraclePagingDao accessOraclePagingDao;

    private final int maxThreadCount = 5;
    private int currThreadCount = 0;
    private final List<Future<String>> threadResultList = new ArrayList<>();
    private ExecutorService es = Executors.newFixedThreadPool(maxThreadCount);
    //导出数据到CSV文件
    private BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("E:\\programs\\demo\\oracleConnect\\access.csv"))));
    private ExportToCSVImRunnable exportToFile = new ExportToCSVImRunnable(bw, 10000);


    public AccessMultiThreadServiceImpl() throws FileNotFoundException {
    }

    @Override
    public void exportAccess2CSV() throws IOException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        loadDB();

        exportToFile.flush(bw);
        es.shutdown();
        stopWatch.stop();
        System.out.println("完成任务耗时：" + stopWatch.getTime() + "ms");
    }

    private void loadDB() throws FileNotFoundException {
        Collection<Access> accesses = accessDao.getAccess();
        //Collection<Access> accesses = accessOraclePagingDao.getAccess();
        List<JSONObject> jsonList = new ArrayList<>(accesses.size());
        Iterator it = accesses.iterator();

        while (it.hasNext()) {
            Access access = (Access) it.next();
            JSONObject json = new JSONObject();
            json.put("d_obj", access.getD_obj());
            json.put("order", access.getOrder());
            json.put("columns", String.valueOf(access.getColumns()));
            json.put("types", access.getTypes());
            jsonList.add(json);
            call(json);
        }
    }

    private void call(JSONObject json) throws FileNotFoundException {
        //添加数据，如果超出了缓存数据，则开始写入文件系统
        if (exportToFile.add(json)) {
            currThreadCount++;
            // 如果提交的线程过多，则取回之后再提交.
            if (currThreadCount >= maxThreadCount) {
                for (Future<String> ignored : threadResultList) {
                    currThreadCount--;
                }
                threadResultList.clear(); // 清空
                currThreadCount = threadResultList.size();
            }
            Future<String> future = (Future<String>) es.submit(exportToFile);
            threadResultList.add(future);
        }

    }

}
