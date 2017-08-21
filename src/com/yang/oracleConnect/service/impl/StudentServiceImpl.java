package com.yang.oracleConnect.service.impl;

import com.yang.oracleConnect.common.ExportToCSVImRunnable;
import com.yang.oracleConnect.common.StopWatch;
import com.yang.oracleConnect.dao.StudentDao;
import com.yang.oracleConnect.entity.Student;
import com.yang.oracleConnect.service.StudentService;
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
 */
@Service
@Transactional(transactionManager = "transactionManager")
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentDao studentDao;

    private final int maxThreadCount = 5;
    private int currThreadCount = 0;
    private final List<Future<String>> threadResultList = new ArrayList<>();
    private ExecutorService es = Executors.newFixedThreadPool(maxThreadCount);
    private BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("E:\\programs\\demo\\oracleConnect\\students.csv"))));
    private ExportToCSVImRunnable exportToFile = new ExportToCSVImRunnable(bw, 10);

    public StudentServiceImpl() throws FileNotFoundException {
    }

    @Override
    public void exportStudent2Excel() throws IOException {
        //ExportToCSVImRunnable exportToFile = new ExportToCSVImRunnable(bw, 10);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        loadDB();

        exportToFile.flush(bw);
        es.shutdown();
        stopWatch.stop();
        System.out.println("完成任务耗时：" + stopWatch.getTime() + "ms");
    }

    private void loadDB() throws FileNotFoundException {
        Collection<Student> students = studentDao.getStudent();
        List<JSONObject> jsonList = new ArrayList<>(students.size());
        Iterator it = students.iterator();

        while (it.hasNext()) {
            Student student = (Student) it.next();
            String stu_id = student.getStuId();
            String stu_name = student.getStuName();
            String gender = student.getGender();
            JSONObject json = new JSONObject();
            json.put("stu_id", stu_id);
            json.put("stu_name", stu_name);
            json.put("gender", gender);
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
            Future<String> future= (Future<String>) es.submit(exportToFile);
            threadResultList.add(future);
        }

    }
}
