package com.czh.life_assistant.util;

import android.content.Context;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class RequestLocationUtil {

    public interface OnRequestLocationListener {

        void startRequestLocation();

        void requestLocationSuccess(Context context, String cityName, String lat_lon);

        void requestLocationFailed();
    }

    public static void requestLocation(final Context context, final OnRequestLocationListener listener) {

        listener.startRequestLocation();

        //声明LocationClient类
        final LocationClient mLocationClient = new LocationClient(context);

        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();

        //注册监听函数
        mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {

                //Log.d("RequestLocationUtil", "onReceiveLocation: " + bdLocation.getLocType());

                if (bdLocation.getLocType() == 161 || bdLocation.getLocType() == 61) {

                    //请求定位成功
                    String Location_country = bdLocation.getCountry();
                    String Location_province = bdLocation.getProvince();
                    String Location_city = bdLocation.getCity();
                    String Location_District = bdLocation.getDistrict();
                    String Location_latLon = bdLocation.getLongitude() + "," + bdLocation.getLatitude();
                    if (Location_District != null) {
                        PrefsUtil.saveInfoToPrefs(context, "selectCity", Location_District + "--" + Location_latLon);
                        listener.requestLocationSuccess(context, Location_District, Location_latLon);
                    } else {
                        PrefsUtil.saveInfoToPrefs(context, "selectCity", Location_city + "--" + Location_latLon);
                        listener.requestLocationSuccess(context, Location_city, Location_latLon);
                    }

                } else {

                    //请求定位失败
                    listener.requestLocationFailed();
                }

                mLocationClient.stop();

            }

            /*
             * i - 当前定位类型
             * i1 - 诊断类型（1~9）
             * s - 具体的诊断信息释义
             * */
            @Override
            public void onLocDiagnosticMessage(int i, int i1, String s) {
                super.onLocDiagnosticMessage(i, i1, s);
            }
        });

    }

}
