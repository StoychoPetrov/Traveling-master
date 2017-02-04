package com.example.stoycho.traveling.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.stoycho.traveling.R;
import com.example.stoycho.traveling.adapters.HotelsAdapter;
import com.example.stoycho.traveling.models.Hotel;
import com.example.stoycho.traveling.tasks.Request;
import com.example.stoycho.traveling.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView        mHotelsListView;
    private HotelsAdapter   mAdapter;
    private List<Hotel>     mHotels;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        initUI(root);
        setListeners();
        mHotels = Utils.getmHotels();
        Collections.sort(mHotels, new Comparator<Hotel>() {
            @Override
            public int compare(Hotel o1, Hotel o2) {
                return ((Double)o1.getmDistance()).compareTo(o2.getmDistance());
            }
        });
        mAdapter = new HotelsAdapter(getActivity(),mHotelsListView,mHotels);
        mHotelsListView.setAdapter(mAdapter);

        return root;
    }

    private void initUI(View root) {
        mHotelsListView = (ListView) root.findViewById(R.id.hotelsListView);
    }

    private void setListeners()
    {
        mHotelsListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HotelFragment newFragment = new HotelFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("hotel",mHotels.get(position));
        newFragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.replace, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
