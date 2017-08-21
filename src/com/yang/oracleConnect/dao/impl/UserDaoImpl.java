package com.yang.oracleConnect.dao.impl;

import com.yang.oracleConnect.dao.IUserDao;
import com.yang.oracleConnect.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * @author Yangjing
 */
@Repository("userDao")
@Transactional(transactionManager = "transactionManager")
public class UserDaoImpl implements IUserDao {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public User login(String userName, String password) {

        String sql = "select * from USERS u where u.username = '" + String.format(userName) + "' and u.password = '" + String.format(password) + "'";
        Query query = entityManager.createNativeQuery(sql);
        List result = query.getResultList();

        if (result != null && result.size() > 0) {
            String name=result.get(1).toString();
            System.out.println(name);
            return new User();
        }
        return null;
    }
}

