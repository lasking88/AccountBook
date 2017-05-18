package com.ohbrothers.www.accountbook.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dgreenhalgh.android.simpleitemdecoration.grid.GridDividerItemDecoration;
import com.ohbrothers.www.accountbook.R;
import com.ohbrothers.www.accountbook.model.DataLab;
import com.ohbrothers.www.accountbook.model.InOutcome;
import com.ohbrothers.www.accountbook.model.MyDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by jk on 4/19/17.
 */

public class InputFragment extends Fragment {

    private static final String CALENDAR_KEY = "Calendar";
    public static final String EXTRA_DATE = "com.ohbrothers.www.acountbook.date";


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
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(CALENDAR_KEY, mCalendar);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_input, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_inoutcome :
                Intent intent = new Intent(getActivity(), InputActivity.class);
                Date today = new Date();
                intent.putExtra(EXTRA_DATE, today);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void update() {
        mMonth = new ArrayList<>();
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int maxdates = mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
        mTitle = mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        mCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int day = 0; day < maxdates * 7; day++) {
            long millis = mCalendar.getTimeInMillis();
            MyDate date = new MyDate(millis);
            if (!mTitle.equals(mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()))) {
                date.setCurrentMonth(false);
            } else {
                date.setCurrentMonth(true);
            }
            DataLab dataLab = DataLab.get(getActivity());
            String key = simpleDateFormat.format(millis);
            date.setInOutcomes(dataLab.getData(key));
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

        private RelativeLayout mLayout;
        private TextView mDateTextView;
        private TextView mIncomeTextView;
        private TextView mOutcomeTextView;

        public DayHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_calendar, parent, false));

            mLayout = (RelativeLayout)itemView.findViewById(R.id.list_item_layout);
            mDateTextView = (TextView)itemView.findViewById(R.id.date_text_view);
            mIncomeTextView = (TextView)itemView.findViewById(R.id.income_text_view);
            mOutcomeTextView = (TextView)itemView.findViewById(R.id.outcome_text_view);
            itemView.setOnClickListener(this);
        }

        public void bind(MyDate myDate) {
            mMyDate = myDate;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d");
            mDateTextView.setText(simpleDateFormat.format(mMyDate));
            if (!mMyDate.isCurrentMonth()) {
                mLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGray));
            } else {
                mLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
            }
            if (mMyDate.getInOutcomes() != null) {
                int income = 0;
                int outcome = 0;
                for(InOutcome ioc : mMyDate.getInOutcomes()) {
                    int inOutcome = ioc.getInOutcome();
                    if (inOutcome > 0) {
                        income += inOutcome;
                    } else {
                        outcome += inOutcome;
                    }
                }
                if (income != 0) {
                    mIncomeTextView.setText("" + income);
                }
                if (outcome != 0) {
                    mOutcomeTextView.setText("" + outcome);
                }
            } else {
                mIncomeTextView.setText("");
                mOutcomeTextView.setText("");
            }
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), InputActivity.class);
            intent.putExtra(EXTRA_DATE, mMyDate);
            startActivity(intent);
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
