/**
 * Copyright 2019 bejson.com
 */
package com.czh.life_assistant.entity.daily_article;
import com.czh.life_assistant.entity.daily_article.Date;

/**
 * Auto-generated: 2019-03-17 22:10:49
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Data {

    private Date date;
    private String author;
    private String title;
    private String digest;
    private String content;
    private int wc;

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getDigest() {
        return digest;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setWc(int wc) {
        this.wc = wc;
    }

    public int getWc() {
        return wc;
    }

}