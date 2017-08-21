package com.yang.oracleConnect.common;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Yangjing
 */
public class ExportToExcelImRunnable implements Runnable {
    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
    private List<org.json.JSONObject> jsonList;
    private XSSFSheet sheet;
    //当前数据量
    private int currItemCount;
    //数据缓存量
    private int dataCacheNum;

    Map<String, Integer> map = getHeader();

    public ExportToExcelImRunnable(XSSFSheet sheet, int dataCacheNum) {
        this.sheet = sheet;
        this.dataCacheNum = dataCacheNum;
        this.jsonList = new ArrayList<>(dataCacheNum);
    }

    //判断缓存是否已满
    private boolean isCacheExpires() {
        return currItemCount >= dataCacheNum;
    }

    public boolean add(org.json.JSONObject jsonObject) {
        try {
            writeLock.lock();
            jsonList.add(jsonObject);
            currItemCount++;
            return isCacheExpires();
        } finally {
            writeLock.unlock();
        }
    }

    public void save(XSSFSheet sheet) {
        try {
            writeLock.lock();

            //如果数据没有超出缓存，则返回
            if (!isCacheExpires()) {
                return;
            }

            int i = 1;
            for (JSONObject json : jsonList) {
                XSSFRow row = sheet.createRow(i++);
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
                currItemCount--;
            }

            jsonList.clear();
        } finally {
            writeLock.unlock();
        }
    }

    public void flush(XSSFSheet sheet) {
        int i = 1;
        for (JSONObject json : jsonList) {
            XSSFRow row = sheet.createRow(i++);
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
        }
        jsonList.clear();
    }

    private Map<String, Integer> getHeader() {
        Map<String, Integer> map = new HashMap<>();
        XSSFWorkbook wb = null;
        try {
            wb = new XSSFWorkbook(new FileInputStream(new File("E:\\programs\\demo\\oracleConnect\\accesses.xlsx")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        XSSFSheet sheet = wb != null ? wb.getSheetAt(0) : null;
        XSSFRow rowHeader = sheet.getRow(0);
        int len = rowHeader.getPhysicalNumberOfCells();
        String[] param = new String[len];
        for (int i = 0; i < param.length; i++) {
            param[i] = rowHeader.getCell(i).getStringCellValue();
            map.put(param[i], i);
        }
        return map;
    }

    @Override
    public void run(){
        save(sheet);
    }
}
