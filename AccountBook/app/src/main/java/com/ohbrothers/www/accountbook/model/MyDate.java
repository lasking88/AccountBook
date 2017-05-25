package com.ohbrothers.www.accountbook.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jk on 4/22/17.
 */

public class MyDate extends Date implements Serializable {

    private boolean isCurrentMonth = false;
    private List<InOutcome> mInOutcomes;

    public MyDate(long date) {
        super(date);
        mInOutcomes = new ArrayList<>();
    }

    public boolean isCurrentMonth() {
        return isCurrentMonth;
    }

    public void setCurrentMonth(boolean currentMonth) {
        isCurrentMonth = currentMonth;
    }

    public List<InOutcome> getInOutcomes() {
        return mInOutcomes;
    }

    public void setInOutcomes(List<InOutcome> inOutcomes) {
        mInOutcomes = inOutcomes;
    }

    public void addInOutcome(InOutcome io) {
        mInOutcomes.add(io);
    }
}
