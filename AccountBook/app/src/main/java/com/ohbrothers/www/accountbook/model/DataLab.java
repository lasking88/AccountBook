package com.ohbrothers.www.accountbook.model;

import java.util.Calendar;

/**
 * Created by jk on 4/22/17.
 */

public class DataLab {

    private static final DataLab ourInstance = new DataLab();

    public static DataLab getInstance() {
        return ourInstance;
    }

    private DataLab() {
    }
}
