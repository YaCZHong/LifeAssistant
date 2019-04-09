package com.czh.life_assistant.util;

public class RequestTranslateUtil {

    private static final String APP_ID = "20190313000276559";
    private static final String SECURITY_KEY = "MiVg169cuqALSRcjICoi";
    private static TransApi api = new TransApi(APP_ID, SECURITY_KEY);

    public static void getTrans(String query, String src, String dst, TransApi.OnTransApiListener listener) {
        api.getTransResult(query, src, dst,listener);
    }
}
