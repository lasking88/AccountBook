package com.ohbrothers.www.accountbook.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
}
