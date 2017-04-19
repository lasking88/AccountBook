package com.ohbrothers.www.accountbook;

import android.app.ActionBar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;

public class MainPagerActivity extends AppCompatActivity {

    private static final int FRAGMENTS_SIZE = 5;

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
    }
}
