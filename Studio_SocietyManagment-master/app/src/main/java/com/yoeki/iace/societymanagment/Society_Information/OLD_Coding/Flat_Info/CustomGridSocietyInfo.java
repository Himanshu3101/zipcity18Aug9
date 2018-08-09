package com.yoeki.iace.societymanagment.Society_Information.OLD_Coding.Flat_Info;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yoeki.iace.societymanagment.R;

class CustomGridSocietyInfo extends BaseAdapter{
    private Context mContext;
    private final String[] SocietyInfo;
    private final int[] SocietyInfoIcon;

    public CustomGridSocietyInfo(Context c,String[] SocietyInfo,int[] SocietyInfoIcon ) {
        mContext = c;
        this.SocietyInfoIcon = SocietyInfoIcon;
        this.SocietyInfo = SocietyInfo;
    }

    @Override
    public int getCount() {
        return SocietyInfo.length;
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

        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.societyinfo_gridview, null);
            TextView textView = (TextView) grid.findViewById(R.id.society_text);
            ImageView imageView = (ImageView)grid.findViewById(R.id.society_image);
            textView.setText(SocietyInfo[position]);
            imageView.setImageResource(SocietyInfoIcon[position]);
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}