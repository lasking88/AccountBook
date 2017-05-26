package com.ohbrothers.www.accountbook.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.ohbrothers.www.accountbook.R;
import com.ohbrothers.www.accountbook.model.MyDate;
import com.ohbrothers.www.accountbook.ocr.OcrCaptureActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jk on 4/19/17.
 */

public class ReceiptFragment extends Fragment implements View.OnClickListener{

    private CompoundButton mAutoFocus;
    private CompoundButton mUseFlash;
    private TextView mStatusMessage;
    private TextView mTextValue;
    private Button mConfirmButton;
    private MyDate mMyDate;
    private TextView mDateTextView;
    private SimpleDateFormat mSimpleDateFormat;

    private static final int RC_OCR_CAPTURE = 9003;
    private static final int REQUEST_DATE = 9004;
    private static final String DIALOG_DATE = "DialogDate";
    private static final String KEY_DATE = "deyDate";
    public static final String EXTRA_INOUTCOME = "com.ohbrothers.www.acountbook.inoutcome";
    private static final String TAG = "ReceiptFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long currentTimeMillis = System.currentTimeMillis();
        if (savedInstanceState == null)
            mMyDate = new MyDate(currentTimeMillis);
        else
            mMyDate = (MyDate)savedInstanceState.getSerializable(KEY_DATE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_DATE, mMyDate);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receipt, container, false);

        mDateTextView = (TextView) view.findViewById(R.id.date_text_view);
        mDateTextView.setText(mSimpleDateFormat.format(mMyDate));
        mDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mMyDate);
                dialog.setTargetFragment(ReceiptFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mStatusMessage = (TextView)view.findViewById(R.id.status_message);
        mTextValue = (TextView)view.findViewById(R.id.text_value);

        mAutoFocus = (CompoundButton) view.findViewById(R.id.auto_focus);
        mUseFlash = (CompoundButton) view.findViewById(R.id.use_flash);

        view.findViewById(R.id.confirm_text).setOnClickListener(this);
        mConfirmButton = (Button)view.findViewById(R.id.read_text);
        mConfirmButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.read_text) {
            // launch Ocr capture activity.
            Intent intent = new Intent(getActivity(), OcrCaptureActivity.class);
            intent.putExtra(OcrCaptureActivity.AutoFocus, mAutoFocus.isChecked());
            intent.putExtra(OcrCaptureActivity.UseFlash, mUseFlash.isChecked());

            startActivityForResult(intent, RC_OCR_CAPTURE);
        } else if (v.getId() == R.id.confirm_text) {
            String string = mTextValue.getText().toString();
            String inoutcome = "";
            for (int i = 0; i < string.length(); ++i) {
                if (Character.isDigit(string.charAt(i))) inoutcome += string.charAt(i);
            }
            Intent intent = new Intent(getActivity(), InputActivity.class);
            intent.putExtra(InputFragment.EXTRA_DATE, mMyDate);
            intent.putExtra(EXTRA_INOUTCOME, inoutcome);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String text = data.getStringExtra(OcrCaptureActivity.TextBlockObject);
                    mStatusMessage.setText(R.string.ocr_success);
                    mTextValue.setText(text);
                    mConfirmButton.setEnabled(true);
                    Log.d(TAG, "Text read: " + text);
                } else {
                    mStatusMessage.setText(R.string.ocr_failure);
                    mConfirmButton.setEnabled(false);
                    Log.d(TAG, "No Text captured, intent data is null");
                }
            } else {
                mStatusMessage.setText(String.format(getString(R.string.ocr_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
                mConfirmButton.setEnabled(false);
            }
        } else if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mMyDate.setTime(date.getTime());
            mDateTextView.setText(mSimpleDateFormat.format(mMyDate));
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
