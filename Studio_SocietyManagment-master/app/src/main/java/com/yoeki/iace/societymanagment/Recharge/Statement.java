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


public class Statement extends Fragment {
    AppCompatButton R_FlatLocation;
    StatementRecyclerViewAdapter sadapter;
    private ArrayList<String> StatementList;
    static List<String> FlatList;
    static ArrayList<String> FlatListArray;
    DBHandler db;
    String[] dataFlat;
    ProgressDialog PD;
    String  LocatIds;
    AppCompatTextView location_Stat;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        db = new DBHandler(getContext());
        location_Stat = (AppCompatTextView) getView().findViewById(R.id.location_Stat);
        R_FlatLocation = (AppCompatButton) getView().findViewById(R.id.unit_location);

        PD = new ProgressDialog(getContext());
        PD.setMessage("Loading...");
        PD.setCancelable(false);






        StatementList = new ArrayList<>();
        StatementList.add("17/08/2018");
        StatementList.add("17/08/2018");
        StatementList.add("17/08/2018");
        StatementList.add("17/08/2018");


        RecyclerView recyclerView = getView().findViewById(R.id.statement);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        sadapter = new StatementRecyclerViewAdapter( getActivity() ,StatementList);
        recyclerView.setAdapter(sadapter);


        R_FlatLocation.setOnClickListener(new View.OnClickListener() {
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
                                    location_Stat.setText(checkedItem.toString());
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
        return inflater.inflate(R.layout.statement, container, false);
    }

}