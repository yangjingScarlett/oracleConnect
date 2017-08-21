package com.yang.oracleConnect.dao.impl;

import com.yang.oracleConnect.dao.AccessDao;
import com.yang.oracleConnect.entity.Access;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Yangjing
 */
@Repository
@Transactional(transactionManager = "transactionManager")
public class AccessDaoImpl implements AccessDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List getAccess(){
        List accessCollection;
        String sql="select * from ACCESS$";
        accessCollection=entityManager.createNativeQuery(sql,Access.class).getResultList();

        return accessCollection;

    }

}
