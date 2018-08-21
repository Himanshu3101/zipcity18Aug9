package com.yoeki.iace.societymanagment.Recharge;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.yoeki.iace.societymanagment.R;

import java.util.ArrayList;


public class Recharge extends Fragment {

    RechargeRecyclerViewAdapter radapter;
    private ArrayList<String> RechargeList;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        RechargeList = new ArrayList<>();
        RechargeList.add("T1-204");
        RechargeList.add("T1-206");
        RechargeList.add("T1-209");
        RechargeList.add("T1-212");


        RecyclerView recyclerView = getView().findViewById(R.id.recharge);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        radapter = new RechargeRecyclerViewAdapter( getActivity() ,RechargeList);
        recyclerView.setAdapter(radapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recharge, container, false);
    }

}



