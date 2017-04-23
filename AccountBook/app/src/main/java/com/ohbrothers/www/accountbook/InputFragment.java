package com.ohbrothers.www.accountbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dgreenhalgh.android.simpleitemdecoration.grid.GridDividerItemDecoration;
import com.ohbrothers.www.accountbook.model.MyDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by jk on 4/19/17.
 */

public class InputFragment extends Fragment {

    private static final String CALENDAR_KEY = "Calendar";

    private RecyclerView mRecyclerView;
    private DayAdapter mDayAdapter;
    private Calendar mCalendar;
    private String mTitle;
    private List<MyDate> mMonth;
    private TextView mMonthTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null)
            mCalendar = Calendar.getInstance();
        else
            mCalendar = (Calendar)savedInstanceState.getSerializable(CALENDAR_KEY);
        mCalendar.setFirstDayOfWeek(Calendar.MONDAY);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(CALENDAR_KEY, mCalendar);
    }

    private void update() {
        mMonth = new ArrayList<>();
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int maxdates = mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
        mTitle = mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        mCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        for (int day = 0; day < maxdates * 7; day++) {
            MyDate date = new MyDate(mCalendar.getTimeInMillis());
            mMonth.add(date);
            mCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        mCalendar.add(Calendar.DAY_OF_MONTH, -15);
        mDayAdapter = new DayAdapter(mMonth);
        mRecyclerView.setAdapter(mDayAdapter);
        mMonthTextView.setText(mTitle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input, container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.calander_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),7));
        mMonthTextView = (TextView)view.findViewById(R.id.month_text_view);
        mMonthTextView.setText(mTitle);
        mRecyclerView.addItemDecoration(new GridDividerItemDecoration(
                ContextCompat.getDrawable(getActivity(), R.drawable.verticaldivider),
                ContextCompat.getDrawable(getActivity(), R.drawable.horizontaldivider),
                7
        ));

        ImageButton nextMonth = (ImageButton)view.findViewById(R.id.next_month_button);
        nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendar.add(Calendar.DAY_OF_MONTH, 30);
                update();
            }
        });

        ImageButton previousMonth = (ImageButton)view.findViewById(R.id.previous_month_button);
        previousMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendar.add(Calendar.DAY_OF_MONTH, -30);
                update();
            }
        });
        update();
        return view;
    }

    private class DayHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MyDate mMyDate;

        private TextView mDateTextView;
        private TextView mIncomeTextView;
        private TextView mOutcomeTextView;

        public DayHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_calendar, parent, false));

            mDateTextView = (TextView)itemView.findViewById(R.id.date_text_view);
            mIncomeTextView = (TextView)itemView.findViewById(R.id.income_text_view);
            mOutcomeTextView = (TextView)itemView.findViewById(R.id.outcome_text_view);
            itemView.setOnClickListener(this);
        }

        public void bind(MyDate myDate) {
            mMyDate = myDate;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d");
            mDateTextView.setText(simpleDateFormat.format(mMyDate));
            if (mMyDate.getIncome() != 0) {
                mIncomeTextView.setText("" + mMyDate.getIncome());
            }
            if (mMyDate.getOutcome() != 0) {
                mOutcomeTextView.setText("" + mMyDate.getOutcome());
            }
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), "is clicked", Toast.LENGTH_SHORT).show();
        }
    }

    private class DayAdapter extends RecyclerView.Adapter<DayHolder> {

        private List<MyDate> mMyDates;

        public DayAdapter(List<MyDate> myDates) {
            mMyDates = myDates;
        }

        @Override
        public DayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new DayHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(DayHolder holder, int position) {
            MyDate myDate = mMyDates.get(position);
            holder.bind(myDate);
        }

        @Override
        public int getItemCount() {
            return mMyDates.size();
        }
    }
}
