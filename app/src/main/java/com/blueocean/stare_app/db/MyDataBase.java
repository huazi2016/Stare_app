package com.blueocean.stare_app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.blueocean.stare_app.PersonBo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BlueOcean_hua
 * Date 2017/6/10
 * Nicename 蓝色海洋
 * Desc 分享犹如大海，互联你我他
 */

public class MyDataBase extends SQLiteOpenHelper {

    private static final String DB_NAME = "stare.db"; //数据库名称

    private static final int VERSION = 1; //数据库版本

    public static MyDataBase myDataBase;

    public static final String TABLE_NAME = "person";

    public static MyDataBase getInstances(Context context) {
        if (myDataBase == null) {
            return new MyDataBase(context);
        } else {
            return myDataBase;
        }
    }

    public MyDataBase(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS person (personid integer primary key autoincrement, name varchar(20), score INTEGER, headId INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            //删除老表
            db.execSQL("drop" + TABLE_NAME);
            //重新创建表
            onCreate(db);
        }
    }

    public void insertPersonInfo(String name, int score,int headId) {
        //让数据库可写
        SQLiteDatabase database = getWritableDatabase();
        /*
        类似于HashMap 都有键值对
        key 对应的列表中的某一列的名称,字段
        value 对应字段要插入的值
         */
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("score", score);
        values.put("headId",headId);
        //插入
        database.insert(TABLE_NAME, null, values);
        //插入完成后关闭,以免造成内存泄漏
        database.close();
    }

    public List<PersonBo> queryPersonInfo() {
        //让数据库可写
        SQLiteDatabase database = getReadableDatabase();
        Cursor query = database.query(TABLE_NAME, null, null, null, null, null, null);
        List<PersonBo> queryDatas = new ArrayList<>();
        while (query.moveToNext()){
            PersonBo personBo = new PersonBo();
            personBo.setName(query.getString(query.getColumnIndex("name")));
            personBo.setScore(query.getInt(query.getColumnIndex("score")));
            personBo.setHeadId(query.getInt(query.getColumnIndex("headId")));
            queryDatas.add(personBo);
        }
        query.close();
        database.close();
        return queryDatas;
    }

    public void deletePersonInfo(String name) {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE_NAME, "name=?", new String[]{name});
        database.close();
    }

    public void deleteAllPersonInfo() {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE_NAME, null, null);
        database.close();
    }

    public void updatePersonInfo(String name,int result) {
        //让数据库可写
        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("score", result);
        String whereClause = "name=?";
        String[] whereArgs = new String[] { name };
        database.update(TABLE_NAME, values, whereClause, whereArgs);
        database.close();
    }
}
