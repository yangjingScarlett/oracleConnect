package com.yang.oracleConnect.dao;


import com.yang.oracleConnect.entity.Student;

import java.util.Collection;

/**
 * @author Yangjing
 */
public interface LocalExternalInterfaceDataDao {
    Collection<Student> getData();
}
