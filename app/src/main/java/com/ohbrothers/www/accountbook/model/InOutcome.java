package com.ohbrothers.www.accountbook.model;

import java.io.Serializable;

/**
 * Created by jk on 4/23/17.
 */

public class InOutcome implements Serializable {

    private int mInOutcome = 0;
    private String mDetail;

    public InOutcome(int inOutcome, String detail) {
        mInOutcome = inOutcome;
        mDetail = detail;
    }

    public int getInOutcome() {
        return mInOutcome;
    }

    public void setInOutcome(int inOutcome) {
        mInOutcome = inOutcome;
    }

    public String getDetail() {
        return mDetail;
    }

    public void setDetail(String detail) {
        mDetail = detail;
    }
}
