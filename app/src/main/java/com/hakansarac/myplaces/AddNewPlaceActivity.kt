package com.hakansarac.myplaces

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_add_new_place.*

class AddNewPlaceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_place)


        setSupportActionBar(toolbarAddNewPlace)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)   //add back button
        toolbarAddNewPlace.setNavigationOnClickListener {
            onBackPressed()
        }

        buttonSave.setBackgroundColor(Color.parseColor("#FFFFBA93"))
    }
}