package com.yang.oracleConnect.dao.impl;

import com.yang.oracleConnect.dao.StudentDao;
import com.yang.oracleConnect.entity.Student;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Yangjing
 */
@Repository
@Transactional(transactionManager = "transactionManager")
public class StudentDaoImpl implements StudentDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Collection<Student> getStudent(){
        Collection<Student> studentCollection=new ArrayList<>();

        String sql="select * from student";
        List list=entityManager.createNativeQuery(sql,Student.class).getResultList();

        for (Object aList : list) {
            Student student = (Student) aList;
            studentCollection.add(student);
        }
        return studentCollection;
    }
}
