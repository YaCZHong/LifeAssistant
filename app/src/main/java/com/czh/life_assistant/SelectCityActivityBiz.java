package com.czh.life_assistant;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.czh.life_assistant.entity.forcity.Basic;
import com.czh.life_assistant.entity.forcity.CityJsonParser;
import com.czh.life_assistant.util.ImportDBUtils;
import com.czh.life_assistant.util.OkHttpUtil;
import com.czh.life_assistant.util.PrefsUtil;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SelectCityActivityBiz {
    public interface AddCityListener {

        void onStartFindCity();

        void onCityNameEmpty();

        void onNotFoundCity();

        void onFoundCitySuccess(List<Basic> basicList);

        void onFoundCityError();
    }

    public List<String> getProvince(Context context) {

        SQLiteDatabase db = ImportDBUtils.openDatabase(context);
        List<String> list = new ArrayList<>();

        Cursor cursor = db.query("city", new String[]{"Province_CN"}, null, null, "Province_CN", null, "Province_CN desc");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String province_Name = cursor.getString(cursor.getColumnIndex("Province_CN"));
                list.add(province_Name);
            }
            cursor.close();
            db.close();
            return list;
        }

        return null;
    }

    public List<String> getAdmin_district(Context context, String province) {

        SQLiteDatabase db = ImportDBUtils.openDatabase(context);
        List<String> list = new ArrayList<>();

        System.out.println(province);
        Cursor cursor = db.query("city", new String[]{"Admin_district_CN"}, "Province_CN = ?", new String[]{province}, "Admin_district_CN", null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String admin_district_Name = cursor.getString(cursor.getColumnIndex("Admin_district_CN"));
                list.add(admin_district_Name);
            }
            cursor.close();
            db.close();
            return list;
        }
        return null;
    }

    public List<String> getCity(Context context, String Admin_district) {

        SQLiteDatabase db = ImportDBUtils.openDatabase(context);
        List<String> list = new ArrayList<>();

        Cursor cursor = db.query("city", new String[]{"City_CN"}, "Admin_district_CN = ?", new String[]{Admin_district}, "City_CN", null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String city_Name = cursor.getString(cursor.getColumnIndex("City_CN"));
                list.add(city_Name);
            }
            cursor.close();
            db.close();
            return list;
        }
        return null;
    }

    public void getCityInfo(String cityName, final AddCityListener listener) {
        if (cityName.equals("")) {
            listener.onCityNameEmpty();
        } else {
            listener.onStartFindCity();
            String url = "https://search.heweather.com/find?location=" + cityName + "&key=c1636a1853a145a180167dd3913672a7&group=cn&number=20";
            OkHttpUtil.sendHttpRequest(url, new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    try {
                        if (CityJsonParser.getHeWeather6(responseData).getStatus().equals("unknown city")) {
                            //查询不到该城市
                            listener.onNotFoundCity();
                        } else {
                            List<Basic> basicList = CityJsonParser.getHeWeather6(responseData).getBasic();
                            //获取到城市列表
                            if (basicList != null) {
                                listener.onFoundCitySuccess(basicList);
                            } else {
                                listener.onNotFoundCity();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    listener.onFoundCityError();
                }
            });
        }

    }

    public String[] getCityFromDb(Context context, String city, String admin_district, String province) {
        SQLiteDatabase db = ImportDBUtils.openDatabase(context);
        String[] cityInfo = new String[3];

        String selection = "City_CN = ? and Admin_district_CN = ? and Province_CN = ?";
        Cursor cursor = db.query("city", new String[]{"City_CN", "Admin_district_CN", "Province_CN", "Country_CN", "Latitude", "Longitude"}, selection, new String[]{city, admin_district, province}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String Latitude = cursor.getString(cursor.getColumnIndex("Latitude"));
                String Longitude = cursor.getString(cursor.getColumnIndex("Longitude"));
                String cityName = cursor.getString(cursor.getColumnIndex("City_CN"));
                String admin_district_Name = cursor.getString(cursor.getColumnIndex("Admin_district_CN"));
                String province_Name = cursor.getString(cursor.getColumnIndex("Province_CN"));
                String country_Name = cursor.getString(cursor.getColumnIndex("Country_CN"));

                cityInfo[0] = cityName;
                cityInfo[1] = admin_district_Name + "--" + province_Name + "--" + country_Name;
                cityInfo[2] = Longitude + "," + Latitude;
            }
            cursor.close();
            db.close();
            return cityInfo;
        }
        return null;
    }

    public void saveSelectCityLatLon(Context context, String cityInfo) {

        //cityInfo = cityName + "--" + cityLatLon ;

        PrefsUtil.saveInfoToPrefs(context, "selectCity", cityInfo);
    }
}
