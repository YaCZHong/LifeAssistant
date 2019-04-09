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
public class Result {

    private Hourly hourly;
    private Realtime realtime;
    private String forecast_keypoint;
    private double primary;
    private Daily daily;

    public Alert getAlert() {
        return alert;
    }

    public void setAlert(Alert alert) {
        this.alert = alert;
    }

    private Alert alert;
    private Minutely minutely;
    public void setHourly(Hourly hourly) {
         this.hourly = hourly;
     }
     public Hourly getHourly() {
         return hourly;
     }

    public void setRealtime(Realtime realtime) {
         this.realtime = realtime;
     }
     public Realtime getRealtime() {
         return realtime;
     }

    public void setForecast_keypoint(String forecast_keypoint) {
         this.forecast_keypoint = forecast_keypoint;
     }
     public String getForecast_keypoint() {
         return forecast_keypoint;
     }

    public void setPrimary(double primary) {
         this.primary = primary;
     }
     public double getPrimary() {
         return primary;
     }

    public void setDaily(Daily daily) {
         this.daily = daily;
     }
     public Daily getDaily() {
         return daily;
     }

    public void setMinutely(Minutely minutely) {
         this.minutely = minutely;
     }
     public Minutely getMinutely() {
         return minutely;
     }

}