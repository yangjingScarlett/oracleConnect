package com.yang.oracleConnect.service.impl;

import com.yang.oracleConnect.common.ExportToExcelImRunnable;
import com.yang.oracleConnect.dao.AccessDao;
import com.yang.oracleConnect.dao.AccessOraclePagingDao;
import com.yang.oracleConnect.entity.Access;
import com.yang.oracleConnect.service.AccessMTExcelService;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
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
 */
@Repository
@Transactional(transactionManager = "transactionManager")
public class AccessMTExcelServiceImpl implements AccessMTExcelService {
    @Autowired
    private AccessDao accessDao;

    @Autowired
    private AccessOraclePagingDao accessOraclePagingDao;

    @Value("${cims.export.accesses}")
    private String accesses;
    @Value("${cims.export.exportAccesses}")
    private String exportAccesses;

    private final int maxThreadCount = 5;
    private int currThreadCount = 0;
    private final List<Future<String>> threadResultList = new ArrayList<>();
    private ExecutorService es = Executors.newFixedThreadPool(maxThreadCount);

    private XSSFSheet sheet;
    private ExportToExcelImRunnable exportToExcel = new ExportToExcelImRunnable(sheet, 1000);

    public AccessMTExcelServiceImpl() throws IOException {
    }

    public void exportAccess2Excel() throws IOException {
        long start = System.currentTimeMillis();
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(accesses)));
        sheet = workbook.getSheetAt(0);
        loadDB();

        exportToExcel.flush(sheet);
        es.shutdown();
        //写入excel文件
        FileOutputStream os = new FileOutputStream(new File(exportAccesses));
        workbook.write(os);
        os.flush();
        os.close();
        System.out.println("ok");
        long stop = System.currentTimeMillis();
        long time = stop - start;
        System.out.println("程序运行耗时: " + time + " ms");
    }

    private void loadDB() throws FileNotFoundException {
        int num = 0;
        for (int i = 0; num % 10000 == 0; i++) {
            Collection<Access> accesses = accessOraclePagingDao.getPartOfAccess(i);
            num = accesses.size();
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
    }

    private void call(JSONObject json) throws FileNotFoundException {
        //添加数据，如果超出了缓存数据，则开始写入文件系统
        if (exportToExcel.add(json)) {
            currThreadCount++;
            // 如果提交的线程过多，则取回之后再提交.
            if (currThreadCount >= maxThreadCount) {
                for (Future<String> ignored : threadResultList) {
                    currThreadCount--;
                }
                threadResultList.clear(); // 清空
                currThreadCount = threadResultList.size();
            }
            Future<String> future = (Future<String>) es.submit(exportToExcel);
            threadResultList.add(future);
        }

    }
}
