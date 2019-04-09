/**
  * Copyright 2018 bejson.com 
  */
package com.czh.life_assistant.entity.forweather;

/**
 * Auto-generated: 2018-12-28 12:20:31
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Wind {

    private double direction;
    private double speed;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Max getMax() {
        return max;
    }

    public void setMax(Max max) {
        this.max = max;
    }

    public Min getMin() {
        return min;
    }

    public void setMin(Min min) {
        this.min = min;
    }

    public Avg getAvg() {
        return avg;
    }

    public void setAvg(Avg avg) {
        this.avg = avg;
    }

    private String date;
    private Max max;
    private Min min;
    private Avg avg;

    public void setDirection(double direction) {
        this.direction = direction;
    }
    public double getDirection() {
        return direction;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
    public double getSpeed() {
        return speed;
    }

}