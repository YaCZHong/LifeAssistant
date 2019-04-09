package com.czh.life_assistant.entity;

import org.litepal.crud.DataSupport;

public class FmsEntity extends DataSupport {

    private int id;
    private String fms_type;
    private String fms_mark;
    private String fms_code;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFms_type() {
        return fms_type;
    }

    public void setFms_type(String fms_type) {
        this.fms_type = fms_type;
    }

    public String getFms_mark() {
        return fms_mark;
    }

    public void setFms_mark(String fms_mark) {
        this.fms_mark = fms_mark;
    }

    public String getFms_code() {
        return fms_code;
    }

    public void setFms_code(String fms_code) {
        this.fms_code = fms_code;
    }
}
