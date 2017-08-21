package com.yang.oracleConnect.controller;

import com.yang.oracleConnect.service.LocalExternalInterfaceDataService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author Yangjing
 */
@RestController
@RequestMapping(value = "/api/data", method = RequestMethod.GET)
public class ExternalInterfaceDataController {
    @Autowired
    private LocalExternalInterfaceDataService localExternalInterfaceDataService;

    @RequestMapping(value = "externalInterfaceData", produces = "application/json")
    public String getJson2Excel() throws IOException, InvalidFormatException, InterruptedException {
        localExternalInterfaceDataService.exportDataToFile();
        return "Success";
    }
}
