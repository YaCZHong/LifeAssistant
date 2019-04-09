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
public class Astro {

    private Date date;
    private Sunset sunset;
    private Sunrise sunrise;
    public void setDate(Date date) {
         this.date = date;
     }
     public Date getDate() {
         return date;
     }

    public void setSunset(Sunset sunset) {
         this.sunset = sunset;
     }
     public Sunset getSunset() {
         return sunset;
     }

    public void setSunrise(Sunrise sunrise) {
         this.sunrise = sunrise;
     }
     public Sunrise getSunrise() {
         return sunrise;
     }

}