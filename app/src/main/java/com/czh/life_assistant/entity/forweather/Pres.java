/**
  * Copyright 2018 bejson.com 
  */
package com.czh.life_assistant.entity.forweather;
import java.util.Date;

/**
 * Auto-generated: 2018-12-28 12:20:31
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Pres {

    private Date date;
    private double max;
    private double avg;
    private double min;
    public void setDate(Date date) {
         this.date = date;
     }
     public Date getDate() {
         return date;
     }

    public void setMax(double max) {
         this.max = max;
     }
     public double getMax() {
         return max;
     }

    public void setAvg(double avg) {
         this.avg = avg;
     }
     public double getAvg() {
         return avg;
     }

    public void setMin(double min) {
         this.min = min;
     }
     public double getMin() {
         return min;
     }

}