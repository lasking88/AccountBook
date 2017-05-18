package com.ohbrothers.www.accountbook.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Pair;

import com.ohbrothers.www.accountbook.database.DbSchema.DataTable;
import com.ohbrothers.www.accountbook.model.InOutcome;

/**
 * Created by jk on 5/15/17.
 */

public class DataCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public DataCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Pair<String, InOutcome> getInOutcome() {
        String detail = getString(getColumnIndex(DataTable.Cols.DETAIL));
        String date = getString(getColumnIndex(DataTable.Cols.DATE));
        int inoutcome = getInt(getColumnIndex(DataTable.Cols.INOUTCOME));

        Pair<String, InOutcome> data = new Pair<>(date, new InOutcome(inoutcome, detail));

        return data;
    }
}
