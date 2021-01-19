package com.hakansarac.myplaces.activities

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.hakansarac.myplaces.R
import com.hakansarac.myplaces.adapters.MyPlacesAdapter
import com.hakansarac.myplaces.database.DatabaseHandler
import com.hakansarac.myplaces.models.PlaceModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fabAddNewPlace.setOnClickListener {
            val intent = Intent(this, AddNewPlaceActivity::class.java)
            startActivityForResult(intent,ADD_PLACE_ACTIVITY_REQUEST_CODE)
        }
        getMyPlacesListFromDB()
    }

    private fun getMyPlacesListFromDB(){
        val dbHandler = DatabaseHandler(this)
        val myPlaceList = dbHandler.getAllMyPlaces()
        if(myPlaceList.size>0){
            recyclerViewMyPlacesList.visibility = View.VISIBLE
            textViewNoPlace.visibility = View.GONE
            setupMyPlacesRecyclerView(myPlaceList)
        }else{
            recyclerViewMyPlacesList.visibility = View.GONE
            textViewNoPlace.visibility = View.VISIBLE
        }
    }

    private fun setupMyPlacesRecyclerView(myPlaceList: ArrayList<PlaceModel>){
        recyclerViewMyPlacesList.layoutManager = LinearLayoutManager(this)
        recyclerViewMyPlacesList.setHasFixedSize(true)

        val placesAdapter = MyPlacesAdapter(this,myPlaceList)
        recyclerViewMyPlacesList.adapter = placesAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == ADD_PLACE_ACTIVITY_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                getMyPlacesListFromDB()
            }else{
                Log.e("Activity","Cancelled or back pressed.")
            }
        }
    }

    companion object{
        var ADD_PLACE_ACTIVITY_REQUEST_CODE = 1
    }
}