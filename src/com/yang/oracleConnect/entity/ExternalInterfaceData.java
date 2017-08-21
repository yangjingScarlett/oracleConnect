package com.yang.oracleConnect.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Clob;

/**
 * @author Yangjing
 */
@Entity
@Table(name = "external_interface_data")
public class ExternalInterfaceData {

    @Id
    @Column(name = "CUSTOMERID")
    private String customerid;

    @Column(name = "CERTID")
    private String certid;

    @Column(name = "MOBILE")
    private String mobile;

    @Column(name = "RESULTDATA")
    private Clob resultdata;

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getCertid() {
        return certid;
    }

    public void setCertid(String certid) {
        this.certid = certid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Clob getResultdata() {
        return resultdata;
    }

    public void setResultdata(Clob resultdata) {
        this.resultdata = resultdata;
    }

    public ExternalInterfaceData() {
    }

    public ExternalInterfaceData(String customerid, String certid, String mobile, Clob resultdata) {
        this.customerid = customerid;
        this.certid = certid;
        this.mobile = mobile;
        this.resultdata = resultdata;
    }
}
