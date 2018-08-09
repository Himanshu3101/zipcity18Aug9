package com.yoeki.iace.societymanagment.Society_Information.New;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yoeki.iace.societymanagment.R;

import java.util.ArrayList;


class CustomGridFacilityInfo extends BaseAdapter{
    private Context mContext;
    private final String[] FacilityInfogrid;
    private ArrayList<String> FacilityNUmber;
    private ArrayList<String> FacilityName;


    public CustomGridFacilityInfo(Context c, ArrayList<String> Facilitygrid  ) {
        mContext = c;
        this.FacilityInfogrid = Facilitygrid.toArray(new String[0]);
    }

    @Override
    public int getCount() {
        return FacilityInfogrid.length;
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
        FacilityNUmber = new ArrayList<String>();
        FacilityName = new ArrayList<String>();
        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.facilitygridview, null);
            AppCompatTextView facility_name = (AppCompatTextView) grid.findViewById(R.id.facility_nme);
            AppCompatTextView factility_no = (AppCompatTextView) grid.findViewById(R.id.factility_no);

            try {
                for (int i = 0; i < FacilityInfogrid.length; ) {
                    String[] splited = FacilityInfogrid[i].split(",");
                    FacilityNUmber.add(splited[0]);
                    FacilityName.add(splited[1]);
                    i++;
                }
            }catch(Exception e){
                e.printStackTrace();
            }

            facility_name.setText(FacilityNUmber.get(position));
            factility_no.setText(FacilityName.get(position));

        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}