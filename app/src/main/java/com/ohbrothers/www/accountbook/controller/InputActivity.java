package com.ohbrothers.www.accountbook.controller;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ohbrothers.www.accountbook.R;
import com.ohbrothers.www.accountbook.model.DataLab;
import com.ohbrothers.www.accountbook.model.InOutcome;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InputActivity extends AppCompatActivity {

    private Date mDate;
    private List<InOutcome> mInOutcomeList;
    private RecyclerView mRecyclerView;
    private int mSelectedPos = -1;
    private View mPreviousView;
    private EditText mDetailInOutcome;
    private EditText mCostInOutcome;
    private Button mAddButton;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButtonIncome;
    private RadioButton mRadioButtonOutcome;
    private SimpleDateFormat mSdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        mDate = (Date)getIntent().getExtras().get(InputFragment.EXTRA_DATE);
        mSdf = new SimpleDateFormat("yyyy-MM-dd");
        final String date = mSdf.format(mDate);
        setTitle(date);

        DataLab dataLab = DataLab.get(getApplicationContext());

        mInOutcomeList = new ArrayList<>();
        mInOutcomeList = dataLab.getData(date);

        mRadioGroup = (RadioGroup)findViewById(R.id.radio_group_inoutcome);
        mRadioButtonIncome = (RadioButton)findViewById(R.id.income_radio_button);
        mRadioButtonOutcome = (RadioButton)findViewById(R.id.outcome_radio_button);
        mDetailInOutcome = (EditText)findViewById(R.id.inoutcome_detail_edit_text);
        mCostInOutcome = (EditText)findViewById(R.id.inoutcome_cost_edit_text);

        String inoutcome = (String)getIntent().getExtras().get(ReceiptFragment.EXTRA_INOUTCOME);
        if (inoutcome != null) {
            mRadioButtonIncome.setChecked(false);
            mRadioButtonOutcome.setChecked(true);
            mCostInOutcome.setText(inoutcome);
        }

        mAddButton = (Button)findViewById(R.id.add_button);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDetailInOutcome.getText().toString().equals("") || mCostInOutcome.getText().toString().equals("")) {
                    Toast.makeText(InputActivity.this, R.string.fill_in_the_blank, Toast.LENGTH_SHORT).show();
                } else if (mRadioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(InputActivity.this, R.string.check_inoutcome, Toast.LENGTH_SHORT).show();
                } else {
                    int cost = Integer.parseInt(mCostInOutcome.getText().toString());
                    if (mRadioButtonOutcome.isChecked()) {
                        cost = -cost;
                    }
                    InOutcome ioc = new InOutcome(cost, mDetailInOutcome.getText().toString());
                    DataLab dataLab = DataLab.get(getApplicationContext());
                    dataLab.addData(date, ioc);
                    finish();
                }
            }
        });

        final Button removeButton = (Button)findViewById(R.id.remove_button);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedPos == -1) {
                    Toast.makeText(InputActivity.this, R.string.choose_one_of_inoutcome, Toast.LENGTH_SHORT).show();
                } else {
                    DataLab dataLab = DataLab.get(getApplicationContext());
                    dataLab.removeDate(date, mInOutcomeList.get(mSelectedPos));
                    finish();
                }
            }
        });

        if (mInOutcomeList != null) {
            mRecyclerView = (RecyclerView)findViewById(R.id.inoutcome_daily_recycler_view);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setAdapter(new DayAdapter(mInOutcomeList));
        } else {
            removeButton.setVisibility(View.GONE);
        }
    }

    private class DayHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private InOutcome mInOutcome;

        private TextView mDetailTextView;
        private TextView mCostTextView;
        private int mPosition;

        public DayHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_detail, parent, false));
            mDetailTextView = (TextView)itemView.findViewById(R.id.daily_inoutcome_title_text_view);
            mCostTextView = (TextView)itemView.findViewById(R.id.daily_inoutcome_cost_text_view);
            itemView.setOnClickListener(this);
        }

        public void bind(InOutcome inOutcome, int position) {
            mInOutcome = inOutcome;
            mPosition = position;
            mDetailTextView.setText(mInOutcome.getDetail());
            int cost = mInOutcome.getInOutcome();
            if (cost > 0) {
                mCostTextView.setText("" + cost);
                mCostTextView.setTextColor(ContextCompat.getColor(InputActivity.this, R.color.colorGreen));
            } else if (cost < 0) {
                mCostTextView.setText("" + -cost);
                mCostTextView.setTextColor(ContextCompat.getColor(InputActivity.this, R.color.colorRed));
            }
        }

        @Override
        public void onClick(View v) {
            if (mPreviousView != null) {
                mPreviousView.setBackground(null);
            }
            mSelectedPos = mPosition;
            v.setBackgroundColor(ContextCompat.getColor(InputActivity.this, R.color.colorGray));
            mPreviousView = v;
            mDetailInOutcome.setText(mInOutcome.getDetail());
            int value = mInOutcome.getInOutcome();
            if (value > 0) {
                mRadioButtonIncome.setChecked(true);
                mRadioButtonOutcome.setChecked(false);
                mCostInOutcome.setText(value + "");
            } else {
                mRadioButtonIncome.setChecked(false);
                mRadioButtonOutcome.setChecked(true);
                mCostInOutcome.setText(-value + "");
            }

            mAddButton.setText("EDIT");
            mAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataLab dataLab = DataLab.get(getApplicationContext());
                    String date = mSdf.format(mDate);
                    dataLab.removeDate(date, mInOutcomeList.get(mSelectedPos));
                    if (mDetailInOutcome == null || mCostInOutcome == null) {
                        Toast.makeText(InputActivity.this, R.string.fill_in_the_blank, Toast.LENGTH_SHORT).show();
                    } else if (mRadioGroup.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(InputActivity.this, R.string.check_inoutcome, Toast.LENGTH_SHORT).show();
                    } else {
                        int cost = Integer.parseInt(mCostInOutcome.getText().toString());
                        if (mRadioButtonOutcome.isChecked()) {
                            cost = -cost;
                        }
                        InOutcome ioc = new InOutcome(cost, mDetailInOutcome.getText().toString());
                        dataLab.addData(date, ioc);
                        finish();
                    }
                }
            });
        }
    }

    private class DayAdapter extends RecyclerView.Adapter<DayHolder> {

        private List<InOutcome> mInOutcomes;

        public DayAdapter(List<InOutcome> inOutcomes) {
            mInOutcomes = inOutcomes;
        }

        @Override
        public DayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(InputActivity.this);
            return new DayHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(DayHolder holder, int position) {
            InOutcome inOutcome = mInOutcomes.get(position);
            holder.bind(inOutcome, position);
        }

        @Override
        public int getItemCount() {
            return mInOutcomes.size();
        }
    }
}
