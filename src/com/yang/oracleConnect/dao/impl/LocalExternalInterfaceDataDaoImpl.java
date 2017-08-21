package com.yang.oracleConnect.dao.impl;


import com.yang.oracleConnect.dao.LocalExternalInterfaceDataDao;
import com.yang.oracleConnect.entity.Student;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**
 * @author Yangjing
 */
@Repository
@Transactional(transactionManager = "transactionManager")
public class LocalExternalInterfaceDataDaoImpl implements LocalExternalInterfaceDataDao {
    @PersistenceContext
    private EntityManager entityManager;

    public Collection<Student> getData() {
        Collection<Student> studentCollection = new ArrayList<>();

        String sql = "select * from student";
        List list = entityManager.createNativeQuery(sql, Student.class).getResultList();

        Iterator it = list.iterator();
        while (it.hasNext()) {
            Student student = (Student) it.next();
            studentCollection.add(student);
        }
        return studentCollection;
    }
}
