package com.yoeki.iace.societymanagment.ComplaintNew;

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
import com.yoeki.iace.societymanagment.societymanagement.ComplaintManagement;

import java.util.ArrayList;
import java.util.List;

public class ComplaintManagementTab extends AppCompatActivity{

    private TabLayout CN_tabLayout;
    private ViewPager CN_viewPager;
    Button CN_bck;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complaint_managementtab);

        CN_bck = (Button) findViewById(R.id.cn_back);

        CN_viewPager = (ViewPager) findViewById(R.id.cn_pager);
        setupViewPager(CN_viewPager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CN_tabLayout = (TabLayout) findViewById(R.id.cn_tabLayout);
        CN_tabLayout.addTab(CN_tabLayout.newTab().setText("Complaint"));
        CN_tabLayout.addTab(CN_tabLayout.newTab().setText("Complaint Closed"));

        CN_tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        CN_tabLayout.setupWithViewPager(CN_viewPager);

        CN_bck.setOnClickListener(new View.OnClickListener() {
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
        adapter.addFragment(new com.yoeki.iace.societymanagment.ComplaintNew.ComplaintManagement(), "Complaint");
        adapter.addFragment(new ClosedComplainttab(), "Complaint Closed");

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