package com.hakansarac.myplaces.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.hakansarac.myplaces.models.PlaceModel

//creating the database logic, extending the SQLiteOpenHelper base class
class DatabaseHandler(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "MyPlacesDatabase"        // Database name
        private const val TABLE_MY_PLACE = "MyPlacesTable"          // Table Name

        //All the Columns names
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
        //creating table with fields
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

    /**
     * Function to insert a Happy Place details to SQLite Database.
     */
    fun addMyPlace(myPlace: PlaceModel): Long {
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

    /**
     * Function to delete happy place details.
     */
    fun deleteMyPlace(myPlace:PlaceModel):Int{
        val db = this.writableDatabase
        // Deleting Row
        val success = db.delete(TABLE_MY_PLACE, KEY_ID + "=" + myPlace.id,null) //2nd argument is String containing nullColumnHack
        db.close()
        return success
    }

    /**
     * Function to update record
     */
    fun updateMyPlace(myPlace: PlaceModel): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, myPlace.title)
        contentValues.put(KEY_IMAGE, myPlace.image)
        contentValues.put(KEY_DESCRIPTION,myPlace.description)
        contentValues.put(KEY_DATE, myPlace.date)
        contentValues.put(KEY_LOCATION, myPlace.location)
        contentValues.put(KEY_LATITUDE, myPlace.latitude)
        contentValues.put(KEY_LONGITUDE, myPlace.longitude)

        // Updating Row
        val success = db.update(TABLE_MY_PLACE,contentValues, KEY_ID + "=" + myPlace.id,null)   //2nd argument is String containing nullColumnHack

        db.close()
        return success
    }

    /**
     * Function to read all the list of Happy Places data which are inserted.
     */
    fun getAllMyPlaces(): ArrayList<PlaceModel>{
        // A list is initialize using the data model class in which we will add the values from cursor.
        val allPlacesList : ArrayList<PlaceModel> = ArrayList<PlaceModel>()
        val selectQuery = "SELECT * FROM $TABLE_MY_PLACE"
        val db = this.readableDatabase

        try{
            val cursor : Cursor =db.rawQuery(selectQuery,null)
            if(cursor.moveToNext()){
                do{
                    val place = PlaceModel(
                            cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                            cursor.getString(cursor.getColumnIndex(KEY_TITLE)),
                            cursor.getString(cursor.getColumnIndex(KEY_IMAGE)),
                            cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)),
                            cursor.getString(cursor.getColumnIndex(KEY_DATE)),
                            cursor.getString(cursor.getColumnIndex(KEY_LOCATION)),
                            cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE)),
                            cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE))
                    )
                    allPlacesList.add(place)
                }while(cursor.moveToNext())
            }
            cursor.close()
        }catch(e: SQLiteException){
            db.execSQL(selectQuery)
            return ArrayList()      //return empty ArrayList
        }
        return allPlacesList
    }

}