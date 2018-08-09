package com.yoeki.iace.societymanagment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class CustomGridHomePage extends BaseAdapter{
    private Context mContext;
    private final String[] SocietyName;
    private final int[] SocietyIcon;

    public CustomGridHomePage(Context c, String[] SocietyName, int[] SocietyIcon ) {
        mContext = c;
        this.SocietyIcon = SocietyIcon;
        this.SocietyName = SocietyName/*.toArray(new String[0])*/;
    }

    @Override
    public int getCount() {
        return SocietyName.length;
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
            grid = inflater.inflate(R.layout.homepagegridview, null);
            TextView textView = (TextView) grid.findViewById(R.id.home_text);
            ImageView imageView = (ImageView)grid.findViewById(R.id.Home_image);
            textView.setText(SocietyName[position]);
            imageView.setImageResource(SocietyIcon[position]);
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}