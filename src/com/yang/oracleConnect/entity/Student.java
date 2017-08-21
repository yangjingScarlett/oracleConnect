package com.yang.oracleConnect.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Yangjing
 */
@Entity
@Table(name="STUDENT")
public class Student {

    @Id
    @Column(name = "STU_ID")
    private String stuId;

    @Column(name = "STU_NAME")
    private String stuName;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "BIRTHDATE")
    private Date birthdate;

    @Column(name = "ENTERDATE")
    private Date enterdate;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "SUBJECT")
    private String subject;

    @Column(name = "CLASSNO")
    private String classno;

    public Student() {
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public Date getEnterdate() {
        return enterdate;
    }

    public void setEnterdate(Date enterdate) {
        this.enterdate = enterdate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getClassno() {
        return classno;
    }

    public void setClassno(String classno) {
        this.classno = classno;
    }
}
