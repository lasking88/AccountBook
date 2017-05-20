package com.ohbrothers.www.accountbook.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ohbrothers.www.accountbook.R;
import com.ohbrothers.www.accountbook.model.DataLab;

public class ChangePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        final EditText newPassword = (EditText)findViewById(R.id.new_passcode_edit_text);
        final EditText confirmPassword = (EditText)findViewById(R.id.confirm_passcode_edit_text);
        TextView ok = (TextView)findViewById(R.id.ok_button);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = newPassword.getText().toString();
                if (password.equals(confirmPassword.getText().toString())) {
                    DataLab dataLab = DataLab.get(getApplicationContext());
                    dataLab.setPasscode(password);
                    Toast.makeText(getApplicationContext(), R.string.password_is_changed, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.password_is_not_matched, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
