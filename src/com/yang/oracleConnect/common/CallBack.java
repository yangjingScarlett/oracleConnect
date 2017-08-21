package com.yang.oracleConnect.common;

/**
 * @author Yangjing
 */
public interface CallBack<T> {
    T call(int num, String str);
}
