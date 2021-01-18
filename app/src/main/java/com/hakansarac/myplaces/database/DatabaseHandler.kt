package com.hakansarac.myplaces.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.hakansarac.myplaces.models.PlaceModel

class DatabaseHandler(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "MyPlacesDatabase"
        private const val TABLE_MY_PLACE = "MyPlacesTable"

        private const val KEY_ID = "_id"
        private const val KEY_TITLE = "title"
        private const val KEY_IMAGE = "image"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_DATE = "date"
        private const val KEY_LOCATION = "location"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val CREATE_MY_PLACE_TABLE = (
                "CREATE TABLE " +
                TABLE_MY_PLACE + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_TITLE + " TEXT," +
                KEY_IMAGE + " TEXT," +
                KEY_DESCRIPTION + " TEXT," +
                KEY_DATE + " TEXT," +
                KEY_LOCATION + " TEXT," +
                KEY_LATITUDE + " TEXT," +
                KEY_LONGITUDE + " TEXT)")
        p0?.execSQL(CREATE_MY_PLACE_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0!!.execSQL("DROP TABLE IF EXISTS $TABLE_MY_PLACE")
        onCreate(p0)
    }

    fun addHappyPlace(myPlace: PlaceModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, myPlace.title)
        contentValues.put(KEY_IMAGE, myPlace.image)
        contentValues.put(KEY_DESCRIPTION,myPlace.description)
        contentValues.put(KEY_DATE, myPlace.date)
        contentValues.put(KEY_LOCATION, myPlace.location)
        contentValues.put(KEY_LATITUDE, myPlace.latitude)
        contentValues.put(KEY_LONGITUDE, myPlace.longitude)

        val result = db.insert(TABLE_MY_PLACE, null, contentValues)

        db.close()
        return result
    }
}