package com.ohbrothers.www.accountbook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ohbrothers.www.accountbook.model.DataLab;
import com.ohbrothers.www.accountbook.model.InOutcome;
import com.ohbrothers.www.accountbook.model.MyDate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by jk on 4/19/17.
 */

public class DetailFragment extends Fragment {

    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;

    private TextView mDateTextView;
    private MyDate mMyDate;
    private SimpleDateFormat mSimpleDateFormat;
    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long currentTimeMillis = System.currentTimeMillis();
        mMyDate = new MyDate(currentTimeMillis);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateMyDate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        mDateTextView = (TextView) view.findViewById(R.id.date_text_view);
        mDateTextView.setText(mSimpleDateFormat.format(mMyDate));
        mDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mMyDate);
                dialog.setTargetFragment(DetailFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mRecyclerView = (RecyclerView)view.findViewById(R.id.detail_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateMyDate();

        return view;
    }

    private void updateMyDate() {
        DataLab dataLab = DataLab.get();
        mMyDate.setInOutcomes(dataLab.getData(mSimpleDateFormat.format(mMyDate)));
        if (mMyDate.getInOutcomes() != null)
            mRecyclerView.setAdapter(new DayAdapter(mMyDate.getInOutcomes()));
        else
            mRecyclerView.setAdapter(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mMyDate.setTime(date.getTime());
            mDateTextView.setText(mSimpleDateFormat.format(mMyDate));
            updateMyDate();
        }
    }

    private class DayHolder extends RecyclerView.ViewHolder {

        private InOutcome mInOutcome;

        private TextView mDetailTextView;
        private TextView mCostTextView;

        public DayHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_detail, parent, false));
            mDetailTextView = (TextView)itemView.findViewById(R.id.daily_inoutcome_title_text_view);
            mCostTextView = (TextView)itemView.findViewById(R.id.daily_inoutcome_cost_text_view);
        }

        public void bind(InOutcome inOutcome) {
            mInOutcome = inOutcome;
            mDetailTextView.setText(mInOutcome.getDetail());
            int cost = mInOutcome.getInOutcome();
            if (cost > 0) {
                mCostTextView.setText("" + cost);
                mCostTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
            } else if (cost < 0) {
                mCostTextView.setText("" + -cost);
                mCostTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorRed));
            }
        }
    }

    private class DayAdapter extends RecyclerView.Adapter<DayHolder> {

        private List<InOutcome> mInOutcomes;

        public DayAdapter(List<InOutcome> inOutcomes) {
            mInOutcomes = inOutcomes;
        }

        @Override
        public DayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new DayHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(DayHolder holder, int position) {
            InOutcome inOutcome = mInOutcomes.get(position);
            holder.bind(inOutcome);
        }

        @Override
        public int getItemCount() {
            return mInOutcomes.size();
        }
    }
}
