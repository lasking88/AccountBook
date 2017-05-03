package com.ohbrothers.www.accountbook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dgreenhalgh.android.simpleitemdecoration.linear.DividerItemDecoration;
import com.ohbrothers.www.accountbook.model.DataLab;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jk on 4/19/17.
 */

public class StatisticsFragment extends Fragment {

    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_START_DATE = 0;
    private static final int REQUEST_END_DATE = 1;

    private RadioGroup mRadioGroup;

    private TextView startDateTextView;
    private TextView endDateTextView;

    private Date mStartDate;
    private Date mEndDate;
    private SimpleDateFormat mSimpleDateFormat;

    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long currentTimeMillis = System.currentTimeMillis();
        mStartDate = new Date(currentTimeMillis);
        mEndDate = new Date(currentTimeMillis);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        startDateTextView = (TextView) view.findViewById(R.id.start_date_text_view);
        startDateTextView.setText(mSimpleDateFormat.format(mStartDate));
        startDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mStartDate);
                dialog.setTargetFragment(StatisticsFragment.this, REQUEST_START_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        endDateTextView = (TextView) view.findViewById(R.id.end_date_text_view);
        endDateTextView.setText(mSimpleDateFormat.format(mEndDate));
        endDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mEndDate);
                dialog.setTargetFragment(StatisticsFragment.this, REQUEST_END_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mRadioGroup = (RadioGroup)view.findViewById(R.id.statistics_radio_group);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.weekly_button:
                        if (DataLab.get().retrieveWeeklyData(mStartDate, mEndDate) != null)
                            mRecyclerView.setAdapter(new StatisticsAdapter(DataLab.get().retrieveWeeklyData(mStartDate, mEndDate)));
                        break;
                    case R.id.monthly_button :
                        if (DataLab.get().retrieveMontlyData(mStartDate, mEndDate) != null)
                            mRecyclerView.setAdapter(new StatisticsAdapter(DataLab.get().retrieveMontlyData(mStartDate, mEndDate)));
                        break;
                }
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.statistics_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(getActivity(), R.drawable.horizontaldivider)));

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;

        if (requestCode == REQUEST_START_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mStartDate.setTime(date.getTime());
            startDateTextView.setText(mSimpleDateFormat.format(mStartDate));
            switch (mRadioGroup.getCheckedRadioButtonId()) {
                case R.id.weekly_button :
                    if (DataLab.get().retrieveWeeklyData(mStartDate, mEndDate) != null)
                        mRecyclerView.setAdapter(new StatisticsAdapter(DataLab.get().retrieveWeeklyData(mStartDate, mEndDate)));
                    break;
                case R.id.monthly_button :
                    if (DataLab.get().retrieveMontlyData(mStartDate, mEndDate) != null)
                        mRecyclerView.setAdapter(new StatisticsAdapter(DataLab.get().retrieveMontlyData(mStartDate, mEndDate)));
                    break;
            }
        } else if (requestCode == REQUEST_END_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mEndDate.setTime(date.getTime());
            endDateTextView.setText(mSimpleDateFormat.format(mEndDate));
            switch (mRadioGroup.getCheckedRadioButtonId()) {
                case R.id.weekly_button :
                    if (DataLab.get().retrieveWeeklyData(mStartDate, mEndDate) != null)
                        mRecyclerView.setAdapter(new StatisticsAdapter(DataLab.get().retrieveWeeklyData(mStartDate, mEndDate)));
                    break;
                case R.id.monthly_button :
                    if (DataLab.get().retrieveMontlyData(mStartDate, mEndDate) != null)
                        mRecyclerView.setAdapter(new StatisticsAdapter(DataLab.get().retrieveMontlyData(mStartDate, mEndDate)));
                    break;
            }
        }
    }

    private class StatisticsHolder extends RecyclerView.ViewHolder {

        private String mKey;
        private List<Integer> mStatistics;

        private TextView mTitleTextView;
        private TextView mIncomeTextView;
        private TextView mOutcomeTextView;
        private TextView mTotalTextView;

        public StatisticsHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_statistics, parent, false));
            mTitleTextView = (TextView)itemView.findViewById(R.id.statistics_item_title_text_view);
            mIncomeTextView = (TextView)itemView.findViewById(R.id.statistics_item_income_text_view);
            mOutcomeTextView = (TextView)itemView.findViewById(R.id.statistics_item_outcome_text_view);
            mTotalTextView = (TextView)itemView.findViewById(R.id.statistics_item_total_text_view);
        }

        public void bind(String key, List<Integer> statistics) {
            mKey = key;
            mStatistics = statistics;
            mTitleTextView.setText(mKey);
            mIncomeTextView.setText(mStatistics.get(0).toString());
            mOutcomeTextView.setText(mStatistics.get(1).toString());
            int total = mStatistics.get(2);
            mTotalTextView.setText(String.valueOf(total));
            if (total > 0) {
                mTotalTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
            } else if (total < 0) {
                mTotalTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorRed));
            }
        }
    }

    private class StatisticsAdapter extends RecyclerView.Adapter<StatisticsHolder> {

        private HashMap<String, List<Integer>> mStringListHashMap;
        private Object[] mKeys;

        public StatisticsAdapter(HashMap<String, List<Integer>> stringListHashMap) {
            mStringListHashMap = stringListHashMap;
            mKeys = mStringListHashMap.keySet().toArray();
        }

        @Override
        public StatisticsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new StatisticsHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(StatisticsHolder holder, int position) {
            List<Integer> integers = mStringListHashMap.get(mKeys[position]);
            holder.bind((String)mKeys[position], integers);
        }

        @Override
        public int getItemCount() {
            return mKeys.length;
        }
    }

}
