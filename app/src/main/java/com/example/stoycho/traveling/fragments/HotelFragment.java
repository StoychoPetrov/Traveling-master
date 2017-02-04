package com.example.stoycho.traveling.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stoycho.traveling.R;
import com.example.stoycho.traveling.adapters.ImageViewPagerAdapter;
import com.example.stoycho.traveling.models.Hotel;
import com.example.stoycho.traveling.tasks.LoadImageTask;
import com.example.stoycho.traveling.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class HotelFragment extends Fragment {

    private TextView    mAddress;
    private TextView    mPhone;
    private TextView    mEmail;
    private TextView    mDescription;
    private ViewPager   mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_hotel, container, false);
        initUI(root);
        loadInformation();
        // Inflate the layout for this fragment
        return root;
    }

    private void initUI(View root)
    {
        mAddress        = (TextView) root.findViewById(R.id.address);
        mPhone          = (TextView) root.findViewById(R.id.phone);
        mEmail          = (TextView) root.findViewById(R.id.email);
        mDescription    = (TextView) root.findViewById(R.id.description);
        mViewPager      = (ViewPager)root.findViewById(R.id.viewPagerImages);
    }

    private void loadInformation()
    {
        Hotel hotel = getArguments().getParcelable("hotel");
        if(hotel != null) {
            mAddress.setText(hotel.getmAdress());
            mPhone.setText(hotel.getmPhone());
            mEmail.setText(hotel.getmEmail());
            mDescription.setText(hotel.getmDescription());

            final ArrayList<Drawable> images = new ArrayList<>();
            final ImageViewPagerAdapter imageViewPagerAdapter = new ImageViewPagerAdapter(getActivity(),images,this,mViewPager);
            mViewPager.setAdapter(imageViewPagerAdapter);

            for(int i = 0 ; i < hotel.getmImages().length; i++)
            {
                String imageUrl = Constants.URL_FOR_IMAGE + hotel.getmImages()[i];
                Drawable image = LoadImageTask.laodImage(getActivity(), "hotels", null, imageUrl, new LoadImageTask.OnImageDownload() {
                    @Override
                    public void onDownload(String url, Drawable drawable) {
                        imageViewPagerAdapter.setImages(drawable);
                    }
                });
                if(image != null)
                {
                    imageViewPagerAdapter.setImages(image);
                }
            }
        }
    }
}
