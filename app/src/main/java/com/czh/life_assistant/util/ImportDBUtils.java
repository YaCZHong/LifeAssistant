package com.czh.life_assistant.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImportDBUtils {

    private static final String DB_NAME = "LocalCityInfo.db";
    private static final String PACKAGE_NAME = "com.czh.life_assistant";
    private static final String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + PACKAGE_NAME + "/databases";

    public static SQLiteDatabase openDatabase(Context context) {

        File Path = new File(DB_PATH + "/" + DB_NAME);
        // 查看数据库文件是否存在
        if (Path.exists()) {
            //Log.i("CZH", "存在数据库");
            // 存在则直接返回打开的数据库
            return SQLiteDatabase.openOrCreateDatabase(Path, null);
        } else {
                try {
                    AssetManager am = context.getAssets();
                    InputStream is = am.open("city.db");

                    // 先得到文件的上级目录，并创建上级目录，在创建文件
                    Path.getParentFile().mkdir();
                    Path.createNewFile();
                    FileOutputStream fos = new FileOutputStream(Path);

                    byte[] buffer = new byte[1024];
                    int count = 0;
                    while ((count = is.read(buffer)) > 0) {
                        fos.write(buffer, 0, count);
                    }
                    fos.flush();
                    fos.close();
                    is.close();
                    //Log.i("CZH", "创建成功");
                    return openDatabase(context);

                } catch (IOException e) {
                    e.printStackTrace();
                    //Log.i("CZH", "创建失败");
                    return null;
                }
        }
    }
}
