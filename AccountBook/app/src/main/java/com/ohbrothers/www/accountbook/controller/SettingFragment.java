package com.ohbrothers.www.accountbook.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ohbrothers.www.accountbook.R;
import com.ohbrothers.www.accountbook.database.DbSchema;
import com.ohbrothers.www.accountbook.model.DataLab;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by jk on 4/19/17.
 */

public class SettingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        final Switch passcodeSwitch = (Switch)view.findViewById(R.id.passcode_switch);
        final TextView changePasscode = (TextView)view.findViewById(R.id.change_passcode_text_view);
        changePasscode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(SettingFragment.this.getActivity(), ChangePasswordActivity.class);
                startActivity(newIntent);
            }
        });
        final DataLab dataLab = DataLab.get(getActivity().getApplicationContext());
        if (dataLab.getPasscodeSwitch() != 0) {
            passcodeSwitch.setChecked(true);
            changePasscode.setEnabled(true);
            changePasscode.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
        } else {
            passcodeSwitch.setChecked(false);
            changePasscode.setEnabled(false);
            changePasscode.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGray));
        }

        passcodeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    dataLab.onPasscode();
                    changePasscode.setEnabled(false);
                    changePasscode.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGray));
                } else {
                    dataLab.offPasscode();
                    changePasscode.setEnabled(true);
                    changePasscode.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
                }
            }
        });

        TextView initDatabase = (TextView)view.findViewById(R.id.init_database);
        initDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataLab dataLab = DataLab.get(getActivity().getApplicationContext());
                dataLab.initialize();
                Toast.makeText(getActivity(), "Database is clear", Toast.LENGTH_SHORT).show();
            }
        });

        TextView backupDatabase = (TextView)view.findViewById(R.id.backup_database);
        backupDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(SettingFragment.this.getActivity(), BackupActivity.class);
                startActivity(newIntent);
            }
        });

        return view;
    }


}
