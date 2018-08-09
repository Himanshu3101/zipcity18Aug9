package com.yoeki.iace.societymanagment.Society_Information.New;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yoeki.iace.societymanagment.R;

import java.util.ArrayList;


class CustomGridSocietyInfo extends BaseAdapter{
    private Context mContext;
    private final String[] UnitTypeInfo;
    private ArrayList<String> UnitNUmber;
    private ArrayList<String> UnitName;

    public CustomGridSocietyInfo(Context c, ArrayList<String> SocietyInfo  ) {
        mContext = c;
        this.UnitTypeInfo = SocietyInfo.toArray(new String[0]);
    }

    @Override
    public int getCount() {
        return UnitTypeInfo.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        UnitNUmber = new ArrayList<String>();
        UnitName = new ArrayList<String>();
        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.unittypegridview, null);
            AppCompatTextView UNit_nme = (AppCompatTextView) grid.findViewById(R.id.Unit);
            AppCompatTextView unit_No = (AppCompatTextView) grid.findViewById(R.id.unit_No);

            for (int i = 0;i<UnitTypeInfo.length;){
                String[] splited = UnitTypeInfo[i].split(",");
                UnitNUmber.add(splited[0]);
                UnitName.add(splited[1]);
                i++;
            }
            UNit_nme.setText(UnitNUmber.get(position));
            unit_No.setText(UnitName.get(position));

        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}