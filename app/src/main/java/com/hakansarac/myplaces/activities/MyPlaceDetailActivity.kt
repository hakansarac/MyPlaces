package com.hakansarac.myplaces.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hakansarac.myplaces.R
import com.hakansarac.myplaces.models.PlaceModel
import kotlinx.android.synthetic.main.activity_my_place_detail.*

/**
 * This function is auto created by Android when the Activity Class is created.
 */
class MyPlaceDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_place_detail)

        var myPlaceDetailModel : PlaceModel? = null
        if(intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)){
            myPlaceDetailModel = intent.getParcelableExtra(MainActivity.EXTRA_PLACE_DETAILS) as PlaceModel?
            //myPlaceDetailModel = intent.getSerializableExtra(MainActivity.EXTRA_PLACE_DETAILS) as PlaceModel
        }
        if(myPlaceDetailModel != null){
            setSupportActionBar(toolbarMyPlaceDetail)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true) //back press
            supportActionBar!!.title = myPlaceDetailModel.title
            toolbarMyPlaceDetail.setNavigationOnClickListener {
                onBackPressed()
            }

            imageViewPlaceDetailImage.setImageURI(Uri.parse(myPlaceDetailModel.image))
            textViewPlaceDetailDescription.text = myPlaceDetailModel.description
            textViewPlaceDetailLocation.text = myPlaceDetailModel.location

        }
    }
}