package com.czh.life_assistant;

import android.content.Context;

import com.czh.life_assistant.entity.forcity.Basic;

import java.util.List;

public class SelectCityPresenter implements SelectCityActivityBiz.AddCityListener {

    private SelectCityActivityView selectCityActivityView;
    private SelectCityActivityBiz SelectCityActivityBiz;

    public SelectCityPresenter(SelectCityActivityView selectCityActivityView, SelectCityActivityBiz SelectCityActivityBiz) {
        this.selectCityActivityView = selectCityActivityView;
        this.SelectCityActivityBiz = SelectCityActivityBiz;
    }

    @Override
    public void onStartFindCity() {
        selectCityActivityView.showProgressDialog();
    }

    @Override
    public void onCityNameEmpty() {
        selectCityActivityView.showToast("请输入城市名");
    }

    @Override
    public void onNotFoundCity() {
        selectCityActivityView.closeProgressDialog();
        selectCityActivityView.showToast("抱歉，未能找到该城市，仅支持市级以上的城市以及部分县区城市哦");
    }

    @Override
    public void onFoundCitySuccess(List<Basic> basicList) {
        selectCityActivityView.closeProgressDialog();
        selectCityActivityView.showListDialog(basicList);
    }

    @Override
    public void onFoundCityError() {
        selectCityActivityView.closeProgressDialog();
        selectCityActivityView.showToast("网络异常，请检查网络");
    }

    public void getCityinfo(String cityName) {
        SelectCityActivityBiz.getCityInfo(cityName, this);
    }

    /*
     * 获取本地数据库里将要添加的城市的位置信息
     * */
    public String[] getCityInfoFromDb(Context context, String city, String admin_district, String province) {
        return SelectCityActivityBiz.getCityFromDb(context, city, admin_district, province);
    }

    public List<String> getProvince(Context context) {
        return SelectCityActivityBiz.getProvince(context);
    }

    public List<String> getAdmin_district(Context context, String province) {
        return SelectCityActivityBiz.getAdmin_district(context, province);
    }

    public List<String> getCity(Context context, String Admin_district) {
        return SelectCityActivityBiz.getCity(context, Admin_district);
    }

    public void saveSelectCity(Context context, String cityInfo) {

        //cityInfo = cityName + "--" + cityLatLon ;

        SelectCityActivityBiz.saveSelectCityLatLon(context, cityInfo);
    }

}
