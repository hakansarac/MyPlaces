package com.hakansarac.myplaces

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
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

        //buttonSave.setBackgroundColor(Color.parseColor("#FFFFBA93"))

        dateSetListener = DatePickerDialog.OnDateSetListener{ view, year,month, dayOfMonth ->
            calendar.set(Calendar.YEAR,year)
            calendar.set(Calendar.MONTH,month)
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            updateDateView()
        }
        editTextDate.setOnClickListener(this)
        textViewAddImage.setOnClickListener(this)   //View.OnClickListener inherited
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.editTextDate -> {
                DatePickerDialog(this@AddNewPlaceActivity, dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show()
            }
            R.id.textViewAddImage -> {
                val pictureDialog = AlertDialog.Builder(this)
                pictureDialog.setTitle("Select Action")
                val pictureDialogItems = arrayOf("Select from gallery.","Capture photo from camera.")
                pictureDialog.setItems(pictureDialogItems){ dialog, which ->
                    when(which){
                        0 -> choosePhotoFromGallery()
                        1 -> {}
                    }
                }
                pictureDialog.show()
            }
        }
    }

    private fun choosePhotoFromGallery() {
        Dexter.withActivity(this).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(object: MultiplePermissionsListener{
            override fun onPermissionsChecked(report: MultiplePermissionsReport?){
                if(report!!.areAllPermissionsGranted()){
                    Toast.makeText(this@AddNewPlaceActivity,"Storage permissions are granted",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                showRationalDialogForPermissions()
            }
        }).onSameThread().check()
    }

    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(this).setMessage("It looks like you have turned " +
                "off permission required for this feature. " +
                "It can be enabled under the application settings.").setPositiveButton("GO TO SETTINGS"){_,_ ->
            try {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package",packageName,null)
                intent.data = uri
                startActivity(intent)
            }catch(e:ActivityNotFoundException){
                e.printStackTrace()
            }
        }.setNegativeButton("Cancel"){dialog,_ ->
            dialog.dismiss()
        }.show()
    }

    private fun updateDateView(){
        val calendarFormat = "dd.MM.yyyy"
        val sdf = SimpleDateFormat(calendarFormat,Locale.getDefault())
        editTextDate.setText(sdf.format(calendar.time).toString())
    }
}