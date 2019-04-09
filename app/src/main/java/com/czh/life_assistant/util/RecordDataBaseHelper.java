package com.czh.life_assistant.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.czh.life_assistant.entity.accounts.RecordEntity;

import java.util.ArrayList;

public class RecordDataBaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Record";

    private static final String CREATE_RECORD_DB = "create table Record ("
            + "id integer primary key autoincrement,"
            + "uuid text,"
            + "type integer,"
            + "category text,"
            + "amount double,"
            + "remark text,"
            + "time integer,"
            + "date date )";

    public RecordDataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_RECORD_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addRecord(RecordEntity record) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("uuid", record.getUuid());
        values.put("type", record.getType());
        values.put("category", record.getCategory());
        values.put("amount", record.getAmount());
        values.put("remark", record.getRemark());
        values.put("time", record.getTimeStamp());
        values.put("date", record.getDate());
        db.insert("Record", null, values);
    }

    public void delectRecord(String uuid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Record", "uuid = ?", new String[]{uuid});
    }

    public void editRecord(String uuid, RecordEntity record) {
        delectRecord(uuid);
        record.setUuid(uuid);
        addRecord(record);
    }

    public ArrayList<RecordEntity> readRecords(String dateStr) {
        ArrayList<RecordEntity> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Record where date = ? order by time asc", new String[]{dateStr});

        if (cursor.moveToFirst()) {
            do {
                String uuid = cursor.getString(cursor.getColumnIndex("uuid"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                double amount = cursor.getDouble(cursor.getColumnIndex("amount"));
                String remark = cursor.getString(cursor.getColumnIndex("remark"));
                long time = cursor.getLong(cursor.getColumnIndex("time"));
                String date = cursor.getString(cursor.getColumnIndex("date"));

                RecordEntity record = new RecordEntity();
                record.setUuid(uuid);
                record.setType(type);
                record.setCategory(category);
                record.setAmount(amount);
                record.setRemark(remark);
                record.setTimeStamp(time);
                record.setDate(date);

                arrayList.add(record);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public ArrayList<String> getValidDate() {
        ArrayList<String> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from Record order by date asc", null);

        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndex("date"));

                if (!arrayList.contains(date)) {
                    arrayList.add(date);
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

}
