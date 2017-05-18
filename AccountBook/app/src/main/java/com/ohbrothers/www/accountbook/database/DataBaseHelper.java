package com.ohbrothers.www.accountbook.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ohbrothers.www.accountbook.database.DbSchema.DataTable;

/**
 * Created by jk on 5/15/17.
 */

public class DataBaseHelper extends SQLiteOpenHelper{
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "dataBase.db";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DataTable.NAME + "(" +
        " _id integer primary key autoincrement, " +
                DataTable.Cols.DATE + ", " +
                DataTable.Cols.DETAIL + ", " +
                DataTable.Cols.INOUTCOME +
        ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
