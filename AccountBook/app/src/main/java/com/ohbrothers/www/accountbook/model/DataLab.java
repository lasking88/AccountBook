package com.ohbrothers.www.accountbook.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by jk on 4/22/17.
 */

public class DataLab {

    private static DataLab sDataLab;

    private HashMap<String, List<InOutcome>> mStringMyDateHashMap;

    public static DataLab get() {
        if (sDataLab == null) {
            sDataLab = new DataLab();
        }
        return sDataLab;
    }

    private DataLab() {
        mStringMyDateHashMap = new HashMap<>();
        createFakeData();
    }

    public void addData(String key, InOutcome ioc) {
        if (mStringMyDateHashMap.get(key) == null) {
            List<InOutcome> inOutcomes = new ArrayList<>();
            inOutcomes.add(ioc);
            mStringMyDateHashMap.put(key, inOutcomes);
        } else {
            mStringMyDateHashMap.get(key).add(ioc);
        }
    }

    public void removeDate(String key, InOutcome ioc) {
        List<InOutcome> inOutcomes = mStringMyDateHashMap.get(key);
        if (inOutcomes != null) {
            inOutcomes.remove(ioc);
        }
    }

    public List<InOutcome> getData(String key) {
        return mStringMyDateHashMap.get(key);
    }

    private void createFakeData() {
        InOutcome ioc = new InOutcome(10000, "budget");
        addData("2017-04-03", ioc);
        InOutcome ioc2 = new InOutcome(-10000, "snack");
        addData("2017-05-17", ioc2);
        InOutcome ioc3 = new InOutcome(1000, "snack");
        addData("2017-04-23", ioc3);
        InOutcome ioc4 = new InOutcome(-2000, "snacks");
        addData("2017-04-23", ioc4);
        InOutcome ioc5 = new InOutcome(10000, "Beers");
        addData("2017-01-03", ioc5);
    }

    private List<Integer> dailyStatistics(String key) {
        List<InOutcome> inOutcomes = mStringMyDateHashMap.get(key);
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
        c.setTime(start);
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
}
