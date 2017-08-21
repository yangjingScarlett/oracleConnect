package com.yang.oracleConnect.service.impl;

import com.yang.oracleConnect.dao.IUserDao;
import com.yang.oracleConnect.entity.User;
import com.yang.oracleConnect.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Yangjing
 */
@Service("userService")
@Transactional(transactionManager = "transactionManager")
public class UserServiceImpl implements IUserService {
    @Autowired
    IUserDao userDao;

    @Override
    public User login(String userName, String password) {
        return userDao.login(userName, password);
    }

}
