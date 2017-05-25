package com.ohbrothers.www.accountbook.controller;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.MetadataChangeSet;
import com.ohbrothers.www.accountbook.R;
import com.ohbrothers.www.accountbook.database.DbSchema;
import com.ohbrothers.www.accountbook.model.DataLab;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.app.Activity.RESULT_OK;

/**
 * Created by jk on 4/19/17.
 */

public class SettingFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SettingFragment";
    private static final int REQUEST_CODE_CREATOR = 1;
    private static final int REQUEST_CODE_RESOLUTION = 2;

    private GoogleApiClient mGoogleApiClient;

    private static final int BUF_SZ = 4096;

    @Override
    public void onPause() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_CREATOR:
                // Called after a file is saved to Drive.
                if (resultCode == RESULT_OK) {
                    Log.i(TAG, "Database successfully saved.");
                    Toast.makeText(getActivity(), "Database successfully saved.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

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
                Toast.makeText(getActivity(), R.string.database_is_clear, Toast.LENGTH_SHORT).show();
            }
        });

        TextView backupDatabase = (TextView)view.findViewById(R.id.backup_database);
        backupDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGoogleApiClient == null) {
                    // Create the API client and bind it to an instance variable.
                    // We use this instance as the callback for connection and connection
                    // failures.
                    // Since no account name is passed, the user is prompted to choose.
                    mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                            .addApi(Drive.API)
                            .addScope(Drive.SCOPE_FILE)
                            .addConnectionCallbacks(SettingFragment.this)
                            .addOnConnectionFailedListener(SettingFragment.this)
                            .build();
                }
                // Connect the client. Once connected, the camera is launched.
                mGoogleApiClient.connect();
            }
        });

        return view;
    }

    private ByteArrayOutputStream isToBytesArray(InputStream is) {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        BufferedInputStream bufIS = null;
        if (is != null) try {
            bufIS = new BufferedInputStream(is);
            byte[] buf = new byte[BUF_SZ];
            int cnt;
            while ((cnt = bufIS.read(buf)) >= 0) {
                byteBuffer.write(buf, 0, cnt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufIS != null) bufIS.close();
            } catch (Exception e) {e.printStackTrace();}
        }
        return byteBuffer;
    }

    private void saveFileToDrive() {
        // Start by creating a new contents, and setting a callback.
        Log.i(TAG, "Creating new contents.");
        Drive.DriveApi.newDriveContents(mGoogleApiClient)
                .setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {

                    @Override
                    public void onResult(DriveApi.DriveContentsResult result) {
                        // If the operation was not successful, we cannot do anything
                        // and must
                        // fail.
                        if (!result.getStatus().isSuccess()) {
                            Log.i(TAG, "Failed to create new contents.");
                            return;
                        }
                        // Otherwise, we can write our data to the new contents.
                        Log.i(TAG, "New contents created.");
                        // Get an output stream for the contents.
                        OutputStream outputStream = result.getDriveContents().getOutputStream();

                        try {
                            outputStream.write(isToBytesArray(new FileInputStream(getActivity().getDatabasePath(DbSchema.DataTable.NAME))).toByteArray());
                        } catch (IOException e1) {
                            Log.i(TAG, "Unable to write file contents.");
                        }
                        // Create the initial metadata - MIME type and title.
                        // Note that the user will be able to change the title later.
                        MetadataChangeSet metadataChangeSet = new MetadataChangeSet.Builder()
                                .setMimeType("application/x-sqlite3").setTitle("Database.db").build();
                        // Create an intent for the file chooser, and start it.
                        IntentSender intentSender = Drive.DriveApi
                                .newCreateFileActivityBuilder()
                                .setInitialMetadata(metadataChangeSet)
                                .setInitialDriveContents(result.getDriveContents())
                                .build(mGoogleApiClient);
                        try {
                            startIntentSenderForResult(
                                    intentSender, REQUEST_CODE_CREATOR, null, 0, 0, 0, null);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "Failed to launch file chooser.");
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Called whenever the API client fails to connect.
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());
        if (!result.hasResolution()) {
            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), result.getErrorCode(), 0).show();
            return;
        }
        // The failure has a resolution. Resolve it.
        // Called typically when the app is not yet authorized, and an
        // authorization
        // dialog is displayed to the user.
        try {
            result.startResolutionForResult(getActivity(), REQUEST_CODE_RESOLUTION);
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Exception while starting resolution activity", e);
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "API client connected.");
        saveFileToDrive();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "GoogleApiClient connection suspended");
    }
}
