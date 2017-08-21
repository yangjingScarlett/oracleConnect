package com.yang.oracleConnect.dao;

import com.yang.oracleConnect.entity.User;

/**
 * @author Yangjing
 */
public interface IUserDao {
    User login(String userName, String password);
}
