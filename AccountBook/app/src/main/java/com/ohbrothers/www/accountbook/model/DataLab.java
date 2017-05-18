package com.ohbrothers.www.accountbook.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Pair;

import com.ohbrothers.www.accountbook.database.DataBaseHelper;
import com.ohbrothers.www.accountbook.database.DataCursorWrapper;
import com.ohbrothers.www.accountbook.database.DbSchema;
import com.ohbrothers.www.accountbook.database.DbSchema.DataTable;
import com.ohbrothers.www.accountbook.database.DbSchema.PasscodeTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by jk on 4/22/17.
 */

public class DataLab {

    private static DataLab sDataLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static DataLab get(Context context) {
        if (sDataLab == null) {
            sDataLab = new DataLab(context);
        }
        return sDataLab;
    }

    private DataLab(Context context) {
        mContext = context;
        mDatabase = new DataBaseHelper(mContext).getWritableDatabase();
    }

    public void initialize() {
        mDatabase.delete(DataTable.NAME, null, null);
    }


    public void setPasscode (String password) {
        mDatabase.execSQL("update " + PasscodeTable.NAME + " set " +
                PasscodeTable.Cols.PASSCODE + "=" + password +
                " where " + PasscodeTable.Cols.PASSCODESWITCH + "=1");
    }

    public void offPasscode() {
        mDatabase.execSQL("update " + PasscodeTable.NAME + " set " +
                PasscodeTable.Cols.PASSCODESWITCH + "=1" +
                " where " + PasscodeTable.Cols.PASSCODESWITCH + "=0");
    }

    public void onPasscode() {
        mDatabase.execSQL("update " + PasscodeTable.NAME + " set " +
                PasscodeTable.Cols.PASSCODESWITCH + "=0" +
        " where " + PasscodeTable.Cols.PASSCODESWITCH + "=1");
    }

    public int getPasscodeSwitch() {
        DataCursorWrapper cursor = queryData(PasscodeTable.NAME, null, null);
        int passcodeSwitch;
        try {
            cursor.moveToFirst();
            passcodeSwitch = cursor.getPasscodeSwitch();
        } finally {
            cursor.close();
        }
        return passcodeSwitch;
    }

    public String getPasscode() {
        DataCursorWrapper cursor = queryData(PasscodeTable.NAME, null, null);
        String rt = "";
        try {
            cursor.moveToFirst();
            rt = cursor.getPasscode();
        } finally {
            cursor.close();
        }
        return rt;
    }

    public void addData(String key, InOutcome ioc) {
        ContentValues values = getContentValues(key, ioc);
        mDatabase.insert(DataTable.NAME, null, values);
    }

    public void removeDate(String key, InOutcome ioc) {
        mDatabase.delete(DataTable.NAME,
                DataTable.Cols.DATE + "='" + key + "' AND "
                + DataTable.Cols.DETAIL + "='" + ioc.getDetail() + "' AND "
                + DataTable.Cols.INOUTCOME + "=" + ioc.getInOutcome()
                , null);
    }

    public List<InOutcome> getData(String key) {
        List<InOutcome> inOutcomes = new ArrayList<>();
        String[] args = {key};
        DataCursorWrapper cursor = queryData(DataTable.NAME, DataTable.Cols.DATE + "=?", args);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                inOutcomes.add(cursor.getInOutcome().second);
                Log.d("Data: ", key + "="+cursor.getInOutcome().first + inOutcomes.get(0).getDetail() + inOutcomes.get(0).getInOutcome());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        Log.d("DATA: ", inOutcomes.size() + "");
        if (inOutcomes.size() == 0) return null;
        return inOutcomes;
    }

    private List<Integer> dailyStatistics(String key) {
        List<InOutcome> inOutcomes = getData(key);
        if (inOutcomes == null) return null;
        int income = 0;
        int outcome = 0;
        for (InOutcome ioc : inOutcomes) {
            int inOutcome = ioc.getInOutcome();
            if (inOutcome > 0) {
                income += inOutcome;
            } else {
                outcome += inOutcome;
            }
        }
        int total = income + outcome;
        List<Integer> dailyStatistics = new ArrayList<>();
        dailyStatistics.add(income);
        dailyStatistics.add(outcome);
        dailyStatistics.add(total);
        return dailyStatistics;
    }

    public HashMap<String, List<Integer>> retrieveWeeklyData(Date start, Date end) {
        if (start == null || end == null || start.after(end)) return null;
        HashMap<String, List<Integer>> weeklyInOutcome = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar c = Calendar.getInstance();
        for (c.setTime(start); c.getTime().before(end); c.add(Calendar.DATE, 1)) {
            List<Integer> dailyStatistic = new ArrayList<>();
            String key = sdf.format(c.getTime());
            Calendar c2 = Calendar.getInstance();
            c2.setTime(c.getTime());
            c2.set(Calendar.DAY_OF_WEEK, c2.getFirstDayOfWeek());
            String key2 = sdf.format(c2.getTime());
            c2.add(Calendar.DATE, 6);
            key2 = key2 + "~" + sdf.format(c2.getTime());
            if (dailyStatistics(key) == null) continue;
            if (weeklyInOutcome.get(key2) == null) {
                dailyStatistic.add(dailyStatistics(key).get(0));
                dailyStatistic.add(dailyStatistics(key).get(1));
                dailyStatistic.add(dailyStatistics(key).get(2));
                weeklyInOutcome.put(key2, dailyStatistic);
            } else {
                dailyStatistic.add(dailyStatistics(key).get(0) + weeklyInOutcome.get(key2).get(0));
                dailyStatistic.add(dailyStatistics(key).get(1) + weeklyInOutcome.get(key2).get(1));
                dailyStatistic.add(dailyStatistics(key).get(2) + weeklyInOutcome.get(key2).get(2));
                weeklyInOutcome.put(key2, dailyStatistic);
            }
        }
        return weeklyInOutcome;
    }

    public HashMap<String, List<Integer>> retrieveMontlyData(Date start, Date end) {
        if (start == null || end == null || start.after(end)) return null;
        HashMap<String, List<Integer>> montlyInOutcome = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        for (c.setTime(start); c.getTime().before(end); c.add(Calendar.DATE, 1)) {
            List<Integer> dailyStatistic = new ArrayList<>();
            String key = sdf.format(c.getTime());
            String key2 = sdf2.format(c.getTime());
            if (dailyStatistics(key) == null) continue;
            if (montlyInOutcome.get(key2) == null) {
                dailyStatistic.add(dailyStatistics(key).get(0));
                dailyStatistic.add(dailyStatistics(key).get(1));
                dailyStatistic.add(dailyStatistics(key).get(2));
                montlyInOutcome.put(key2, dailyStatistic);
            } else {
                dailyStatistic.add(dailyStatistics(key).get(0) + montlyInOutcome.get(key2).get(0));
                dailyStatistic.add(dailyStatistics(key).get(1) + montlyInOutcome.get(key2).get(1));
                dailyStatistic.add(dailyStatistics(key).get(2) + montlyInOutcome.get(key2).get(2));
                montlyInOutcome.put(key2, dailyStatistic);
            }
        }
        return montlyInOutcome;
    }

    private static ContentValues getContentValues(String date, InOutcome inOutCome) {
        ContentValues values = new ContentValues();
        values.put(DataTable.Cols.DATE, date);
        values.put(DataTable.Cols.DETAIL, inOutCome.getDetail());
        values.put(DataTable.Cols.INOUTCOME, inOutCome.getInOutcome());
        return values;
    }

    private static ContentValues getPasscodeValues (int passcodeSwitch, String passcode) {
        ContentValues values = new ContentValues();
        values.put(PasscodeTable.Cols.PASSCODESWITCH, passcodeSwitch);
        values.put(PasscodeTable.Cols.PASSCODE, passcode);
        return values;
    }

    private DataCursorWrapper queryData (String database, String whereClause, String[] whereArg) {
        Cursor cursor = mDatabase.query(
                database,
                null,
                whereClause,
                whereArg,
                null,
                null,
                null
        );

        return new DataCursorWrapper(cursor);
    }
}
