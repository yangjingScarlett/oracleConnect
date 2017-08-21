package com.yang.oracleConnect.controller;

import com.yang.oracleConnect.service.AccessMTExcelService;
import com.yang.oracleConnect.service.AccessMultiThreadService;
import com.yang.oracleConnect.service.AccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author Yangjing
 */
@RestController
@RequestMapping("/export/data")
public class AccessDataController {
    //导出到Excel文件
    @Autowired
    private AccessService accessService;

    //导出到CSV文件
    @Autowired
    private AccessMultiThreadService accessMultiThreadService;

    //导出到Excel文件
    @Autowired
    private AccessMTExcelService accessMTExcelService;

    @RequestMapping(value = "/access", produces = "application/json")
    public String getData() throws IOException {
        //accessService.pagingExport2Excel();
        //accessMultiThreadService.exportAccess2CSV();
        accessMTExcelService.exportAccess2Excel();
        return "Success";
    }
}
