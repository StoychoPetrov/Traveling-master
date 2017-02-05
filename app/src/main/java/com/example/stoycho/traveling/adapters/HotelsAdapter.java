package com.example.stoycho.traveling.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.stoycho.traveling.R;
import com.example.stoycho.traveling.models.Hotel;
import com.example.stoycho.traveling.tasks.LoadImageTask;
import com.example.stoycho.traveling.utils.Constants;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by Stoycho on 12/18/2016.
 */

public class HotelsAdapter extends BaseAdapter {

    private Context         mContext;
    private List<Hotel>     mHotels;
    private ListView        mList;
    private LayoutInflater  mInflater;

    public HotelsAdapter(Context mContext,ListView list, List<Hotel> mHotels) {
        this.mContext   = mContext;
        this.mHotels    = mHotels;
        this.mList      = list;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mHotels.size();
    }

    @Override
    public Object getItem(int position) {
        return mHotels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
            convertView = mInflater.inflate(R.layout.hotel_item, parent, false);

        Hotel hotel = mHotels.get(position);
        ImageView logoImg       = (ImageView)   convertView.findViewById(R.id.hotel_logo);
        TextView  nameTxt       = (TextView)    convertView.findViewById(R.id.name);
        TextView  distanceTxt   = (TextView)    convertView.findViewById(R.id.distance);

        String distance = String.format(new Locale("bg"),"%.2f", hotel.getmDistance()) + mContext.getString(R.string.km);
        distanceTxt.setText(distance);
        nameTxt.setText(hotel.getmName());

        if(hotel.getmImages().size() > 0) {
            String logo = Constants.URL_FOR_IMAGE + hotel.getmImages().get(0);
            Drawable drawable = LoadImageTask.laodImage(mContext, "hotels", null, logo, new LoadImageTask.OnImageDownload() {
                @Override
                public void onDownload(String url, Drawable drawable) {
                    for (int i = 0; i < mList.getChildCount(); i++) {
                        View view = mList.getChildAt(i);
                        int pos = mList.getPositionForView(view);
                        if (pos >= 0 && pos < mHotels.size() && mHotels.get(pos).getmImages() != null) {
                            if (mHotels.get(pos).getmImages().equals(url)) {
                                ImageView placeImage = (ImageView) view.findViewById(R.id.hotel_logo);
                                if (placeImage != null)
                                    placeImage.setImageDrawable(drawable);
                                break;
                            }
                        }
                    }
                    HotelsAdapter.this.notifyDataSetChanged();
                }
            });
            if (drawable != null)
                logoImg.setImageDrawable(drawable);
        }

        return convertView;
    }

    public void setHotels(List<Hotel> hotels)
    {
        mHotels = hotels;
    }
}
