package com.yang.oracleConnect.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Yangjing
 */
@Entity
@Table(name = "ACCESS$")
public class Access {
    @Id
    @Column(name = "D_OBJ#")
    private double d_obj;

    @Column(name = "ORDER#")
    private double order;

    @Column(name = "COLUMNS")
    private byte[] columns;

    @Column(name = "TYPES")
    private double types;

    public Access() {
    }

    public double getD_obj() {
        return d_obj;
    }

    public void setD_obj(double d_obj) {
        this.d_obj = d_obj;
    }

    public double getOrder() {
        return order;
    }

    public void setOrder(double order) {
        this.order = order;
    }

    public byte[] getColumns() {
        return columns;
    }

    public void setColumns(byte[] columns) {
        this.columns = columns;
    }

    public double getTypes() {
        return types;
    }

    public void setTypes(double types) {
        this.types = types;
    }
}
