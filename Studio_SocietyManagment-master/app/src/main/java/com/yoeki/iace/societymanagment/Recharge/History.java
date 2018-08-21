package com.yoeki.iace.societymanagment.Recharge;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yoeki.iace.societymanagment.Database.DBHandler;
import com.yoeki.iace.societymanagment.R;

import java.util.ArrayList;
import java.util.List;


public class History extends Fragment {

    HistoryRecyclerViewAdapter hadapter;
    private ArrayList<String> HistoryList;
    DBHandler db;
    String[] dataFlat;
    String  LocatIds;
    ProgressDialog PD;
    AppCompatTextView location_Hist_text;
    static List<String> FlatList;
    static ArrayList<String> FlatListArray;
    AppCompatButton location_Hist;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        db = new DBHandler(getContext());
        location_Hist_text = (AppCompatTextView) getView().findViewById(R.id.location_Hist_text);
        location_Hist = (AppCompatButton) getView().findViewById(R.id.location_Hist);

        PD = new ProgressDialog(getContext());
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        HistoryList = new ArrayList<>();
        HistoryList.add("17/08/2018");
        HistoryList.add("17/08/2018");
        HistoryList.add("17/08/2018");
        HistoryList.add("17/08/2018");


        RecyclerView recyclerView = getView().findViewById(R.id.history);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        hadapter = new HistoryRecyclerViewAdapter( getActivity() ,HistoryList);
        recyclerView.setAdapter(hadapter);

        location_Hist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PD.show();
                FlatList = new ArrayList<String>();
                FlatListArray = new ArrayList<String>();
                FlatList = db.getFlatList();
                for (final String link : FlatList) {
                    String log = link;
                    FlatListArray.add(log);
                }
                dataFlat = FlatListArray.toArray(new String[0]);
                try {
                    new AlertDialog.Builder(getContext(), AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                            .setTitle("Select Location")
                            .setSingleChoiceItems(dataFlat, 0, null)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                    int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                                    ListView lw = ((AlertDialog)dialog).getListView();
                                    Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                                    location_Hist_text.setText(checkedItem.toString());
                                    PD.dismiss();

                                    try{
                                        LocatIds = String.valueOf(db.getFlatListID(checkedItem.toString()));
                                    }catch(Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .show();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.history, container, false);
    }

}