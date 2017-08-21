package com.yang.oracleConnect.controller;

import com.yang.oracleConnect.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author Yangjing
 */

@RestController
@RequestMapping("/export/data")
public class StudentDataController {
    @Autowired
    private StudentService studentService;

    @RequestMapping(value = "/student", produces = "application/json")
    public String export() throws IOException {
        studentService.exportStudent2Excel();
        return "Success";
    }
}
