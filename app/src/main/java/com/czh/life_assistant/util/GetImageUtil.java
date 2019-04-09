package com.czh.life_assistant.util;

import android.content.Context;

import com.czh.life_assistant.R;


/**
 * Created by czh on 2018/5/6.
 */

public class GetImageUtil {

    public static int getImage(Context context, String weather) {
        int imageId;

        switch (weather) {
            case "CLEAR_DAY":
                imageId = R.drawable.ic_default_sun;
                break;
            case "CLEAR_NIGHT":
                imageId = R.drawable.ic_default_sun_night;
                break;
            case "CLOUDY":
                imageId = R.drawable.ic_default_overcast;
                break;
            case "PARTLY_CLOUDY_DAY":
                imageId = R.drawable.ic_default_cloudy;
                break;
            case "PARTLY_CLOUDY_NIGHT":
                imageId = R.drawable.ic_default_cloudy_night;
                break;
            case "RAIN":
                imageId = R.drawable.ic_default_rain;
                break;
            case "SNOW":
                imageId = R.drawable.ic_default_snow;
                break;
            case "WIND":
                imageId = R.drawable.ic_default_wind;
                break;
            case "HAZE":
                imageId = R.drawable.ic_default_haze;
                break;
            default:
                imageId = R.drawable.ic_default_na_w;
                break;
        }
        return imageId;
    }
}

