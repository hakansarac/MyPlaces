package com.hakansarac.myplaces

import android.app.DatePickerDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_add_new_place.*
import java.text.SimpleDateFormat
import java.util.*

class AddNewPlaceActivity : AppCompatActivity(), View.OnClickListener {

    private var calendar = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_place)



        setSupportActionBar(toolbarAddNewPlace)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)   //add back button
        toolbarAddNewPlace.setNavigationOnClickListener {
            onBackPressed()
        }

        buttonSave.setBackgroundColor(Color.parseColor("#FFFFBA93"))

        dateSetListener = DatePickerDialog.OnDateSetListener{ view, year,month, dayOfMonth ->
            calendar.set(Calendar.YEAR,year)
            calendar.set(Calendar.MONTH,month)
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            updateDateView()
        }
        editTextDate.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.editTextDate -> {
                DatePickerDialog(this@AddNewPlaceActivity, dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show()
            }
        }
    }

    private fun updateDateView(){
        val calendarFormat = "dd.MM.yyyy"
        val sdf = SimpleDateFormat(calendarFormat,Locale.getDefault())
        editTextDate.setText(sdf.format(calendar.time).toString())
    }
}