package com.yang.oracleConnect.dao;

import java.util.List;

/**
 * @author Yangjing
 */
public interface AccessOraclePagingDao {
    List getAccess();

    List getPartOfAccess(int i);
}
