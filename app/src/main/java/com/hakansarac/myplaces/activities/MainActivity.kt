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

        //to update our list members dynamically when user adds new place and comes back main activity
        //we use startActivityForResult
        fabAddNewPlace.setOnClickListener {
            val intent = Intent(this, AddNewPlaceActivity::class.java)
            startActivityForResult(intent,ADD_PLACE_ACTIVITY_REQUEST_CODE)
        }
        getMyPlacesListFromDB()     //shows place list saved in database
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

        placesAdapter.setOnClickListener(object:MyPlacesAdapter.OnClickListener{
            override fun onClick(position: Int, model: PlaceModel) {
                val intent = Intent(this@MainActivity,MyPlaceDetailActivity::class.java)
                intent.putExtra(EXTRA_PLACE_DETAILS,model)
                //to do that parcelable or serializable are implemented to PlaceModel class, otherwise the function does not know the type of model
                startActivity(intent)
            }
        })
    }

    //to show database dynamically, when user adds new place to list.
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
        var EXTRA_PLACE_DETAILS = "extra_place_details"
    }
}