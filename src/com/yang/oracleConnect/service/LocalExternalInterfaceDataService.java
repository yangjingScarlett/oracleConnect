package com.yang.oracleConnect.service;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;

/**
 * @author Yangjing
 */
public interface LocalExternalInterfaceDataService {
    void exportDataToFile() throws IOException, InvalidFormatException, InterruptedException;
}
