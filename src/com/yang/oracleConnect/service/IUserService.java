package com.yang.oracleConnect.service;

import com.yang.oracleConnect.entity.User;

/**
 * @author Yangjing
 */
public interface IUserService {
    User login(String userName, String password);
}
