/**
 * Copyright 2019 bejson.com
 */
package com.czh.life_assistant.entity.forweather;

import java.util.List;

/**
 * Auto-generated: 2019-01-15 15:10:34
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Alert {

    private String status;
    private List<Content> content;

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }

    public List<Content> getContent() {
        return content;
    }

}