package com.example.stoycho.traveling.utils;

import com.example.stoycho.traveling.models.Hotel;

import java.util.List;

/**
 * Created by Stoycho on 12/18/2016.
 */

public class Utils {

    private static List<Hotel> mHotels;

    public static List<Hotel> getmHotels()
    {
        return mHotels;
    }

    public static void setmHotels(List<Hotel> hotels)
    {
        mHotels = hotels;
    }

}
