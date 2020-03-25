package com.loop;

import java.util.List;

public class Transaction {
    private String header_id;
    private String code;
    private String date;
    private String customer_id;
    private String customer_name;
    private String agen_id;
    private String reference;
    private List<Detail> detail;

    public Transaction() {}

    public Transaction(String header_id, String code, String date, String customer_id, String customer_name, String agen_id, String reference, List<Detail> detail) {
        this.header_id = header_id;
        this.code = code;
        this.date = date;
        this.customer_id = customer_id;
        this.customer_name = customer_name;
        this.agen_id = agen_id;
        this.reference = reference;
        this.detail = detail;
    }

    public String getHeader_id() {
        return header_id;
    }

    public void setHeader_id(String header_id) {
        this.header_id = header_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getAgen_id() {
        return agen_id;
    }

    public void setAgen_id(String agen_id) {
        this.agen_id = agen_id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public List<Detail> getDetail() {
        return detail;
    }

    public void setDetail(List<Detail> detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "header_id='" + header_id + '\'' +
                ", code='" + code + '\'' +
                ", date='" + date + '\'' +
                ", customer_id='" + customer_id + '\'' +
                ", customer_name='" + customer_name + '\'' +
                ", agen_id='" + agen_id + '\'' +
                ", reference='" + reference + '\'' +
                ", detail=" + detail +
                '}';
    }
}
