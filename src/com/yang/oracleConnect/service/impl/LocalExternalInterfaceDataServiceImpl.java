package com.yang.oracleConnect.service.impl;

import com.yang.oracleConnect.common.PoiWriterImRunnable;
import com.yang.oracleConnect.dao.LocalExternalInterfaceDataDao;
import com.yang.oracleConnect.entity.Student;
import com.yang.oracleConnect.service.LocalExternalInterfaceDataService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Yangjing
 */
@Service
@Transactional(transactionManager = "transactionManager")
public class LocalExternalInterfaceDataServiceImpl implements LocalExternalInterfaceDataService {
    @Autowired
    private LocalExternalInterfaceDataDao localExternalInterfaceDataDao;

    @Value("${cims.template.url}")
    private String template;

    @Value("${cims.json2excel.url}")
    private String json2excel;

    public LocalExternalInterfaceDataServiceImpl() throws IOException {
    }


    @Override
    public void exportDataToFile() throws IOException, InvalidFormatException, InterruptedException {
        //使用线程池进行线程管理
        ExecutorService es = Executors.newCachedThreadPool();

        //使用计数栅栏
        CountDownLatch doneSignal = new CountDownLatch(3);
        FileOutputStream os;

        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File("E:\\programs\\demo\\oracleConnect\\template.xlsx")));
        XSSFSheet sheet = workbook.getSheetAt(0);

        Collection<Student> students = localExternalInterfaceDataDao.getData();
        JSONObject[] jsonArray = new JSONObject[students.size()];
        Iterator iterator = students.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            //获取与数据库数据对应的json数据
            Student student = (Student) iterator.next();

            //每一项都要初始化，否则会有nullPointerException
            jsonArray[i] = new JSONObject();
            jsonArray[i].put("STU_ID", student.getStuId());
            jsonArray[i].put("STU_NAME", student.getStuName());
            jsonArray[i].put("GENDER", student.getGender());
            jsonArray[i].put("BIRTHDATE", student.getBirthdate());
            jsonArray[i].put("ENTERDATE", student.getEnterdate());
            jsonArray[i].put("ADDRESS", student.getAddress());
            jsonArray[i].put("SUBJECT", student.getSubject());
            jsonArray[i].put("CLASSNO", student.getClassno());
            i++;
        }

        es.submit(new PoiWriterImRunnable(doneSignal, sheet, jsonArray, 2, 29));
        es.submit(new PoiWriterImRunnable(doneSignal, sheet, jsonArray, 30, 59));
        es.submit(new PoiWriterImRunnable(doneSignal, sheet, jsonArray, 60, 101));

        doneSignal.await();
        es.shutdown();
        //写入excel
        os = new FileOutputStream(new File(json2excel));
        workbook.write(os);
        os.flush();
        os.close();
        System.out.println("ok");
    }

}
