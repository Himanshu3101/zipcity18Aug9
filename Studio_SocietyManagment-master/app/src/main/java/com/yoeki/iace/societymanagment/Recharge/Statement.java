package com.yoeki.iace.societymanagment.Recharge;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.yoeki.iace.societymanagment.Database.DBHandler;
import com.yoeki.iace.societymanagment.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Statement extends Fragment {
    AppCompatButton R_FlatLocation,RE_from_date,RE_to_date,clickpopup,view_stat;
    AppCompatEditText Stat_todate,Stat_fromdate;
    StatementRecyclerViewAdapter sadapter;
    private ArrayList<String> StatementList;
    static List<String> FlatList;
    static ArrayList<String> FlatListArray;
    DBHandler db;
    String[] dataFlat;
    ProgressDialog PD;
    int whichdate=0;
    String  LocatIds;
    AppCompatTextView location_Stat;
    private int mYear, mMonth, mDay;
    String date_time,_fromDate, _toDate;
    LinearLayout total_stat;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        db = new DBHandler(getContext());
        location_Stat = (AppCompatTextView) getView().findViewById(R.id.location_Stat);
        R_FlatLocation = (AppCompatButton) getView().findViewById(R.id.unit_location);
        Stat_fromdate = (AppCompatEditText) getView().findViewById(R.id.Stat_fromdate);
        RE_from_date = (AppCompatButton) getView().findViewById(R.id.RE_from_date);
        Stat_todate = (AppCompatEditText) getView().findViewById(R.id.Stat_todate);
        RE_to_date = (AppCompatButton) getView().findViewById(R.id.RE_to_date);
        clickpopup=(AppCompatButton)getView().findViewById(R.id.clickpopup);
        view_stat = (AppCompatButton) getView().findViewById(R.id.view_stat);

        total_stat=(LinearLayout)getView().findViewById(R.id.total_stat);

        PD = new ProgressDialog(getContext());
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        clickpopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent (getActivity(),StatementPopup.class);
                getActivity().startActivity(intent);
            }
        });

        view_stat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              total_stat.setVisibility(View.VISIBLE);
            }
        });

        RE_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
//                Stat_fromdate.setText(date_time);
            }
        });

        RE_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
                whichdate=1;
//                Stat_todate.setText(date_time);
            }
        });

//                                          For Designing Purpose
//        StatementList = new ArrayList<>();
//        StatementList.add("17/08/2018");
//        StatementList.add("17/08/2018");
//        StatementList.add("17/08/2018");
//        StatementList.add("17/08/2018");
//
//
//        RecyclerView recyclerView = getView().findViewById(R.id.statement);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        sadapter = new StatementRecyclerViewAdapter( getActivity() ,StatementList);
//        recyclerView.setAdapter(sadapter);


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

    private void datePicker(){

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date_time = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
                        if (whichdate == 1) {
                            _toDate = date_time;
                            Stat_todate.setText(_toDate);
                            whichdate=0;
                        }else{
                            _fromDate = date_time;
                            Stat_fromdate.setText(_fromDate);
                            whichdate=0;
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.statement, container, false);
    }

}