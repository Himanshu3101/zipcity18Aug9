package com.yoeki.iace.societymanagment.RequestNew;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.yoeki.iace.societymanagment.Home_Page;
import com.yoeki.iace.societymanagment.R;

import java.util.ArrayList;
import java.util.List;

public class RequestManagementTab extends AppCompatActivity{

    private TabLayout RN_tabLayout;
    private ViewPager RN_viewPager;
    Button RN_bck;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_management_tab);

        RN_bck = (Button) findViewById(R.id.rn_back);

        RN_viewPager = (ViewPager) findViewById(R.id.rn_pager);
        setupViewPager(RN_viewPager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RN_tabLayout = (TabLayout) findViewById(R.id.rn_tabLayout);
        RN_tabLayout.addTab(RN_tabLayout.newTab().setText("Request"));
        RN_tabLayout.addTab(RN_tabLayout.newTab().setText("Request Closed"));

        RN_tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        RN_tabLayout.setupWithViewPager(RN_viewPager);

        RN_bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Home_Page.class);
                startActivity(intent);
                finish();
            }
        });

//        SocietyManagementPager adapter = new SocietyManagementPager(getSupportFragmentManager(), tabLayout.getTabCount());
//        viewPager.setAdapter(adapter);
//         tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//             @Override
//             public void onTabSelected(TabLayout.Tab tab) {
//
//             }
//
//             @Override
//             public void onTabUnselected(TabLayout.Tab tab) {
//
//             }
//
//             @Override
//             public void onTabReselected(TabLayout.Tab tab) {
//
//             }
//         });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new RequestManagement(), "Request");
        adapter.addFragment(new ClosedRequestTab(), "Request Closed");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),Home_Page.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }


}