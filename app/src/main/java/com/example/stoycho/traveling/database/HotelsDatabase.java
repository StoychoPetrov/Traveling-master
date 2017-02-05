package com.example.stoycho.traveling.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.stoycho.traveling.models.Hotel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stoycho on 2/4/2017.
 */

public class HotelsDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME   = "HotelsDB";

    /******** Hotels table ***************/
    private static final String TABLE_HOTELS_NAME           = "hotels";
    private static final String COLUMN_HOTEL_ID             = "hotel_id";
    private static final String COLUMN_HOTEL_NAME           = "hotel_name";
    private static final String COLUMN_HOTEL_DESCRIPTION    = "hotel_description";
    private static final String COLUMN_HOTEL_ADRESS         = "hotel_adress";
    private static final String COLUMN_HOTEL_PHONE          = "hotel_phone";
    private static final String COLUMN_HOTEL_EMAIL          = "hotel_email";
    private static final String COLUMN_HOTEL_WEBSITE        = "hotel_website";
    private static final String COLUMN_HOTEL_LATITUDE       = "hotel_latitude";
    private static final String COLUMN_HOTEL_LONGITUDE      = "hotel_longitude";

    private static final String CREATE_HOTELS_TABLE = "CREATE TABLE " + TABLE_HOTELS_NAME + "( " + COLUMN_HOTEL_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_HOTEL_NAME + " TEXT, " + COLUMN_HOTEL_DESCRIPTION + " TEXT, " + COLUMN_HOTEL_ADRESS + " TEXT, " + COLUMN_HOTEL_PHONE + " TEXT, " + COLUMN_HOTEL_EMAIL + " TEXT, "
            + COLUMN_HOTEL_WEBSITE + " TEXT, " + COLUMN_HOTEL_LATITUDE + " DECIMAL, " + COLUMN_HOTEL_LONGITUDE + " REAL)";


    /********* Images table ****************/
    private static final String TABLE_IMAGES_NAME   = "images";
    private static final String COLUMN_IMAGE_ID     = "image_id";
    private static final String COLUMN_IMAGE_PATH   = "image_path";
    private static final String HOTEL_ID_FORIGN_KEY = "hotel_id";

    private static final String CREATE_IMAGES_TABLE = "CREATE TABLE " + TABLE_IMAGES_NAME + "( " + COLUMN_IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_IMAGE_PATH + " TEXT, " + HOTEL_ID_FORIGN_KEY + " INTEGER, FOREIGN KEY (" + HOTEL_ID_FORIGN_KEY + ") REFERENCES "
            + TABLE_HOTELS_NAME + "(" + COLUMN_HOTEL_ID + "))";

    public HotelsDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_HOTELS_TABLE);
        db.execSQL(CREATE_IMAGES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOTELS_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES_NAME);
        onCreate(db);
    }

    public boolean insertIntoHotels(Hotel hotel)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_HOTEL_ID, hotel.getmId());
        values.put(COLUMN_HOTEL_NAME, hotel.getmName());
        values.put(COLUMN_HOTEL_DESCRIPTION, hotel.getmDescription());
        values.put(COLUMN_HOTEL_ADRESS, hotel.getmAdress());
        values.put(COLUMN_HOTEL_PHONE, hotel.getmPhone());
        values.put(COLUMN_HOTEL_EMAIL, hotel.getmEmail());
        values.put(COLUMN_HOTEL_WEBSITE, hotel.getmWebsite());
        values.put(COLUMN_HOTEL_LATITUDE, hotel.getmLatitude());
        values.put(COLUMN_HOTEL_LONGITUDE, hotel.getmLongitude());

        long newRowId = db.insert(TABLE_HOTELS_NAME, null, values);

        for (String imagePath : hotel.getmImages())
        {
            if(!insertIntoImages(imagePath,hotel.getmId()))
                Log.d("Error_inserting","Error when insert image!");
        }
        db.close();
        return newRowId != -1;
    }

    private boolean insertIntoImages(String imagePath,int hotelId)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE_PATH, imagePath);
        values.put(HOTEL_ID_FORIGN_KEY, hotelId);

        long newRowId = db.insert(TABLE_IMAGES_NAME, null, values);
        db.close();
        return newRowId != -1;
    }

    public List<Hotel> selectAllHotels()
    {
        SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_HOTELS_NAME + ";";

        List<Hotel> hotels = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectQuery,null);

        while(cursor.moveToNext()) {
            Hotel hotel = new Hotel();
            hotel.setmId(cursor.getInt(cursor.getColumnIndex(COLUMN_HOTEL_ID)));
            hotel.setmName(cursor.getString(cursor.getColumnIndex(COLUMN_HOTEL_NAME)));
            hotel.setmDescription(cursor.getString(cursor.getColumnIndex(COLUMN_HOTEL_DESCRIPTION)));
            hotel.setmEmail(cursor.getString(cursor.getColumnIndex(COLUMN_HOTEL_EMAIL)));
            hotel.setmAdress(cursor.getString(cursor.getColumnIndex(COLUMN_HOTEL_ADRESS)));
            hotel.setmPhone(cursor.getString(cursor.getColumnIndex(COLUMN_HOTEL_PHONE)));
            hotel.setmWebsite(cursor.getString(cursor.getColumnIndex(COLUMN_HOTEL_WEBSITE)));
            hotel.setmLatitude(cursor.getDouble(cursor.getColumnIndex(COLUMN_HOTEL_LATITUDE)));
            hotel.setmLongitude(cursor.getDouble(cursor.getColumnIndex(COLUMN_HOTEL_LONGITUDE)));

            List<String> images = selectImagesForHotel(hotel.getmId());
            hotel.setmImages(images);
            hotels.add(hotel);
        }

        cursor.close();
        db.close();

        return hotels;
    }

    public List<String> selectImagesForHotel(int hotelId)
    {
        SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT " + COLUMN_IMAGE_PATH + " FROM " + TABLE_IMAGES_NAME + " WHERE " + HOTEL_ID_FORIGN_KEY + "=" + hotelId + ";";

        List<String> images = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectQuery,null);
        while(cursor.moveToNext()) {
            String image = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_PATH));

            images.add(image);
        }

        cursor.close();
        db.close();

        return images;
    }

    public int getCountOfHotels()
    {
        String query = "SELECT COUNT(*) FROM " + TABLE_HOTELS_NAME;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();

        return count;
    }
}
