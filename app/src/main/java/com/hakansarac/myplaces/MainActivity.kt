package com.hakansarac.myplaces

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun buttonAddNewPlace(view:View){
        val intent = Intent(this,AddNewPlaceActivity::class.java)
        startActivity(intent)
    }
}