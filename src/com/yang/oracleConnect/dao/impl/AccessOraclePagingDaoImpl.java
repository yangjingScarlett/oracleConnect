package com.yang.oracleConnect.dao.impl;

import com.yang.oracleConnect.dao.AccessOraclePagingDao;
import com.yang.oracleConnect.entity.Access;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yangjing
 */
@Repository
@Transactional(transactionManager = "transactionManager")
public class AccessOraclePagingDaoImpl implements AccessOraclePagingDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List getAccess() {
        List accessList = new ArrayList<>();

        for (int i = 0; accessList.size() % 10000 == 0; i++) {
            List accesses;
            String sql = "select a.* from (select rownum rn,access$.* from access$) a where a.rn>" + i * 10000 + " and a.rn<=" + (i + 1) * 10000;
            accesses = entityManager.createNativeQuery(sql, Access.class).getResultList();
            accessList.addAll(accesses);
        }
        return accessList;

    }

    @Override
    public List getPartOfAccess(int i) {
        List accesses;
        String sql = "select a.* from (select rownum rn,access$.* from access$) a where a.rn>" + i * 10000 + " and a.rn<=" + (i + 1) * 10000;
        accesses = entityManager.createNativeQuery(sql, Access.class).getResultList();
        return accesses;
    }

}
