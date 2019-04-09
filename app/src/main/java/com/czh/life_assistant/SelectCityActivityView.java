package com.czh.life_assistant;


import com.czh.life_assistant.entity.forcity.Basic;

import java.util.List;

public interface SelectCityActivityView {

    void showProgressDialog();

    void closeProgressDialog();

    void showListDialog(List<Basic> list);

    void showToast(String msg);

    void setResultToMain(Boolean IsSelectCity);
}
