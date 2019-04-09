/**
 * Copyright 2019 bejson.com
 */
package com.czh.life_assistant.entity.trans;

import java.util.List;

/**
 * Auto-generated: 2019-03-18 21:10:5
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class LanguageResult {

    private String from;
    private String to;
    private List<Trans_result> trans_result;

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTo() {
        return to;
    }

    public void setTrans_result(List<Trans_result> trans_result) {
        this.trans_result = trans_result;
    }

    public List<Trans_result> getTrans_result() {
        return trans_result;
    }

}