package com.ohbrothers.www.accountbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private RecyclerView mRecyclerView;
    private Calendar mCalendar;
    private String mTitle;
    private List<MyDate> mMonth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCalendar = Calendar.getInstance();
        update();
    }

    private void update() {
        mMonth = new ArrayList<>();
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);
        mTitle = mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        mCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        for (int day = 1; day < 36; day++) {
            MyDate date = new MyDate(mCalendar.getTimeInMillis());
            mMonth.add(date);
            mCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input, container, false);
        TextView monthTextView = (TextView)view.findViewById(R.id.month_text_view);
        monthTextView.setText(mTitle);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.calander_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),7));
        mRecyclerView.setAdapter(new DayAdapter(mMonth));
        mRecyclerView.addItemDecoration(new GridDividerItemDecoration(
                ContextCompat.getDrawable(getActivity(), R.drawable.verticaldivider),
                ContextCompat.getDrawable(getActivity(), R.drawable.horizontaldivider),
                7
        ));
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
            mIncomeTextView.setText("" + mMyDate.getIncome());
            mOutcomeTextView.setText("" + mMyDate.getOutcome());
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
