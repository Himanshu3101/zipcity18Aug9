package com.yoeki.iace.societymanagment.societymanagement;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class SocietyManagementPager extends FragmentStatePagerAdapter {
    int tabCount;

    public SocietyManagementPager(FragmentManager fm, int tabCount) {
        super(fm);

        this.tabCount= tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ComplaintManagement complaintManagement = new ComplaintManagement();
                return complaintManagement;
            case 1:
                RequestManagement requestManagement = new RequestManagement();
                return requestManagement;

                default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return tabCount;
    }
}