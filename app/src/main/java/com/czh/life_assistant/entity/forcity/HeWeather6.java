package com.czh.life_assistant.entity.forcity;

import java.util.List;

/**
 * Created by czh on 2018/5/4.
 */

public class HeWeather6 {


    private List<Basic> basic;
    private String status;

    public void setBasic(List<Basic> basic) {
        this.basic = basic;
    }

    public List<Basic> getBasic() {
        return basic;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
