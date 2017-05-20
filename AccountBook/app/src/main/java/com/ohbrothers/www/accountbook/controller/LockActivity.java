package com.ohbrothers.www.accountbook.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ohbrothers.www.accountbook.R;
import com.ohbrothers.www.accountbook.model.DataLab;

public class LockActivity extends AppCompatActivity {

    private String mPasscode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DataLab.get(LockActivity.this.getApplicationContext()).getPasscodeSwitch() == 0) {
            Intent newIntent = new Intent(LockActivity.this, MainPagerActivity.class);
            startActivity(newIntent);
            finish();
        }
        setContentView(R.layout.activity_lock);

        final EditText passcode = (EditText)findViewById(R.id.passcode_edit_text);
        TextView ok = (TextView)findViewById(R.id.ok_button);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPasscode = DataLab.get(LockActivity.this.getApplicationContext()).getPasscode();
                if (passcode.getText().toString().equals(mPasscode)) {
                    Intent newIntent = new Intent(LockActivity.this, MainPagerActivity.class);
                    startActivity(newIntent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.wrong_password, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
