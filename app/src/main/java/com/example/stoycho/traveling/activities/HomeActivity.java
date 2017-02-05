package com.example.stoycho.traveling.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.stoycho.traveling.R;
import com.example.stoycho.traveling.adapters.ViewPagerAdapter;
import com.example.stoycho.traveling.fragments.HomeFragment;
import com.example.stoycho.traveling.fragments.MapFragment;
import com.example.stoycho.traveling.models.CustomViewPager;
import com.example.stoycho.traveling.tasks.Request;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends FragmentActivity implements View.OnClickListener, ViewPager.OnPageChangeListener{

    private CustomViewPager viewPager;
    private TextView barTitle;
    private ImageButton mapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initUI();
        setListeners();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new MapFragment());
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(adapter);
    }

    private void initUI()
    {
        viewPager = (CustomViewPager) findViewById(R.id.viewPager);
        barTitle = (TextView)findViewById(R.id.barTitle);
        mapButton = (ImageButton)findViewById(R.id.mapButton);
    }

    private void setListeners()
    {
        viewPager.addOnPageChangeListener(this);
        mapButton.setOnClickListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(position == 0)
            barTitle.setText(getString(R.string.hotels));
        else
            barTitle.setText(getString(R.string.map));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.mapButton:
                onMapButton();
                break;
        }
    }

    private void onMapButton()
    {
        if(viewPager.getCurrentItem() == 0)
            viewPager.setCurrentItem(1);
        else
            viewPager.setCurrentItem(0);
    }

    @Override
    public void onBackPressed()
    {

    }
}
