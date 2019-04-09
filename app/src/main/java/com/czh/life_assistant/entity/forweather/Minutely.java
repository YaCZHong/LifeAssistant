/**
  * Copyright 2018 bejson.com 
  */
package com.czh.life_assistant.entity.forweather;
import java.util.List;

/**
 * Auto-generated: 2018-12-28 12:20:31
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Minutely {

    private String status;
    private String description;
    private List<Double> probability;
    private List<Double> probability_4h;
    private String datasource;
    private List<Double> precipitation_2h;
    private List<Double> precipitation;
    public void setStatus(String status) {
         this.status = status;
     }
     public String getStatus() {
         return status;
     }

    public void setDescription(String description) {
         this.description = description;
     }
     public String getDescription() {
         return description;
     }

    public void setProbability(List<Double> probability) {
         this.probability = probability;
     }
     public List<Double> getProbability() {
         return probability;
     }

    public void setProbability_4h(List<Double> probability_4h) {
         this.probability_4h = probability_4h;
     }
     public List<Double> getProbability_4h() {
         return probability_4h;
     }

    public void setDatasource(String datasource) {
         this.datasource = datasource;
     }
     public String getDatasource() {
         return datasource;
     }

    public void setPrecipitation_2h(List<Double> precipitation_2h) {
         this.precipitation_2h = precipitation_2h;
     }
     public List<Double> getPrecipitation_2h() {
         return precipitation_2h;
     }

    public void setPrecipitation(List<Double> precipitation) {
         this.precipitation = precipitation;
     }
     public List<Double> getPrecipitation() {
         return precipitation;
     }

}