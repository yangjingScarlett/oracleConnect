package com.yang.oracleConnect.service.impl;

import com.yang.oracleConnect.dao.AccessDao;
import com.yang.oracleConnect.entity.Access;
import com.yang.oracleConnect.service.AccessService;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author Yangjing
 */
@Service
@Transactional(transactionManager = "transactionManager")
public class AccessServiceImpl implements AccessService {
    @Autowired
    private AccessDao accessDao;
    //private AccessOraclePagingDao accessDao;

    @Value("${cims.export.accesses}")
    private String accesses;

    @Value("${cims.export.exportAccesses}")
    private String exportAccesses;

    @Override
    public void pagingExport2Excel() throws IOException {
        long start = System.currentTimeMillis();
        List accessList = accessDao.getAccess();
        Iterator it = accessList.iterator();

        int dataCount = accessList.size();
        int PAGE_SIZE = 20000;
        int sheetCount = dataCount % PAGE_SIZE > 0 ? dataCount / PAGE_SIZE + 1 : dataCount / PAGE_SIZE;

        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(accesses)));
        FileOutputStream os = new FileOutputStream(new File(exportAccesses));

        for (int m = 0; m < sheetCount; m++) {
            XSSFSheet sheet = workbook.createSheet("access" + m);
            XSSFRow rowHeader = sheet.createRow(0);
            XSSFCell cell;

            String[] headerArray = new String[]{"d_obj", "order", "columns", "types"};
            for (int j = 0; j < headerArray.length; j++) {
                cell = rowHeader.createCell(j);
                cell.setCellValue(headerArray[j]);
            }

            int rowNum = 1;
            //这里一定要先判断it.hasNext()为真，才能接着做it.next()，否则会报NoSuchElementException
            for (int i = 0; i < PAGE_SIZE && it.hasNext(); i++) {
                Access access = (Access) it.next();
                if (access != null) {
                    int colNo = 0;
                    XSSFRow row = sheet.createRow(rowNum++);
                    row.createCell(colNo++).setCellValue(String.valueOf(access.getD_obj()));
                    row.createCell(colNo++).setCellValue(String.valueOf(access.getOrder()));
                    row.createCell(colNo++).setCellValue(Arrays.toString(access.getColumns()));
                    row.createCell(colNo).setCellValue(String.valueOf(access.getTypes()));
                }
            }

        }

        workbook.write(os);
        os.flush();
        os.close();
        long stop = System.currentTimeMillis();
        long time = stop - start;
        System.out.println("ok");
        System.out.println("程序运行耗时: " + time + " ms");
    }
}
