package com.ohbrothers.www.accountbook.controller;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ohbrothers.www.accountbook.R;

public class MainPagerActivity extends AppCompatActivity {

    private static final int FRAGMENTS_SIZE = 5;
    private static final int READ_SMS_PERMISSION = 9099;

    private ViewPager mViewPager;
    private Fragment[] mFragments = {
            new InputFragment(),
            new DetailFragment(),
            new StatisticsFragment(),
            new ReceiptFragment(),
            new SettingFragment()
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pager);
        mViewPager = (ViewPager) findViewById(R.id.main_view_pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0 :
                        return mFragments[0];
                    case 1 :
                        return mFragments[1];
                    case 2 :
                        return mFragments[2];
                    case 3 :
                        return mFragments[3];
                    case 4 :
                        return mFragments[4];
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return FRAGMENTS_SIZE;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0 :
                        return getResources().getString(R.string.tab0);
                    case 1 :
                        return getResources().getString(R.string.tab1);
                    case 2 :
                        return getResources().getString(R.string.tab2);
                    case 3 :
                        return getResources().getString(R.string.tab3);
                    case 4 :
                        return getResources().getString(R.string.tab4);
                    default:
                        return null;
                }
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tab_layout);
        tabLayout.setupWithViewPager(mViewPager);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_SMS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_SMS},
                        READ_SMS_PERMISSION);

                // READ_SMS_PERMISSION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_SMS_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}
