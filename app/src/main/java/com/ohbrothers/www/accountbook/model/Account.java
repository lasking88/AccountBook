package com.ohbrothers.www.accountbook.model;

import java.util.Currency;
import java.util.UUID;

/**
 * Created by jk on 4/17/17.
 */

public class Account {

    private UUID mId;
    private String mTitle;
    private Currency mCurrency;
    private int mIncome;
    private int mOutcome;

    public Account() {
        mId = UUID.randomUUID();
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Currency getCurrency() {
        return mCurrency;
    }

    public void setCurrency(Currency currency) {
        mCurrency = currency;
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
