package com.ohbrothers.www.accountbook.model;

import java.util.Date;

/**
 * Created by jk on 4/22/17.
 */

public class MyDate extends Date {
    private int mIncome = 0;
    private int mOutcome = 0;

    public MyDate(long date) {
        super(date);
    }

    public int getIncome() {
        return mIncome;
    }

    public void setIncome(int income) {
        mIncome = income;
    }

    public int getOutcome() {
        return mOutcome;
    }

    public void setOutcome(int outcome) {
        mOutcome = outcome;
    }
}
