package com.yang.oracleConnect.common;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class PoiWriterImRunnable implements Runnable {

    private final CountDownLatch doneSignal;

    private final XSSFSheet sheet;

    private final JSONObject[] jsons;

    private final int start;

    private final int end;

    Map<String, Integer> map = getHeader();

    public PoiWriterImRunnable(CountDownLatch doneSignal, XSSFSheet sheet, JSONObject[] jsons, int start, int end) {
        this.doneSignal = doneSignal;
        this.sheet = sheet;
        this.jsons = jsons;
        this.start = start;
        this.end = end;
    }

    public void run() {
        int i = start;
        while (i <= end) {
            XSSFRow row = createNewRow(sheet, i);
            JSONObject json = jsons[i - 2];

            if (json != null) {
                Iterator it = json.keys();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    if (map.containsKey(key)) {
                        int colNo = map.get(key);
                        XSSFCell cell = row.createCell(colNo);
                        cell.setCellValue(String.valueOf(json.get(key)));
                    } else {
                        System.out.println(key);
                    }
                }
            }
            i++;
        }
        doneSignal.countDown();
        System.out.println("start: " + start + " end: " + end + " Count: " + doneSignal.getCount());

    }

    //sheet的row使用treeMap存储的，是非线程安全的，所以在创建row时需要进行同步操作。
    private synchronized XSSFRow createNewRow(XSSFSheet sheet, int rowNum) {
        return sheet.createRow(rowNum);
    }

    private Map<String, Integer> getHeader() {
        Map<String, Integer> map = new HashMap<>();
        XSSFWorkbook wb = null;
        try {
            wb = new XSSFWorkbook(new FileInputStream(new File("E:\\programs\\demo\\oracleConnect\\template.xlsx")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        XSSFSheet sheet = wb != null ? wb.getSheetAt(0) : null;
        XSSFRow rowHeader = sheet.getRow(1);
        int len = rowHeader.getPhysicalNumberOfCells();
        String[] param = new String[len];
        for (int i = 0; i < param.length; i++) {
            param[i] = rowHeader.getCell(i).getStringCellValue();
            map.put(param[i], i);
        }
        return map;
    }
}

