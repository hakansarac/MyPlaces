package com.hakansarac.myplaces.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import com.hakansarac.myplaces.R
import com.hakansarac.myplaces.database.DatabaseHandler
import com.hakansarac.myplaces.models.PlaceModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_add_new_place.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class AddNewPlaceActivity : AppCompatActivity(), View.OnClickListener {

    private var calendar = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    private var saveImageToInternalStorage : Uri? = null
    private var mLatitude : Double = 0.0
    private var mLongitude : Double = 0.0

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
        updateDateView()
        editTextDate.setOnClickListener(this)
        textViewAddImage.setOnClickListener(this)   //View.OnClickListener inherited
        buttonSave.setOnClickListener(this)
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
                        1 -> takePhotoFromCamera()
                    }
                }
                pictureDialog.show()
            }
            R.id.buttonSave -> {
                when{
                    editTextTitle.text.isNullOrEmpty() ->
                        Toast.makeText(this@AddNewPlaceActivity,"Please enter a title.",Toast.LENGTH_SHORT).show()

                    editTextDescription.text.isNullOrEmpty() ->
                        Toast.makeText(this@AddNewPlaceActivity,"Please enter a description.",Toast.LENGTH_SHORT).show()

                    editTextLocation.text.isNullOrEmpty() ->
                        Toast.makeText(this@AddNewPlaceActivity,"Please enter a location.",Toast.LENGTH_SHORT).show()

                    saveImageToInternalStorage == null ->
                        Toast.makeText(this@AddNewPlaceActivity,"Please select an image.",Toast.LENGTH_SHORT).show()
                    else ->{
                        val placeModel = PlaceModel(
                                0,
                                editTextTitle.text.toString(),
                                saveImageToInternalStorage.toString(),
                                editTextDescription.text.toString(),
                                editTextDate.text.toString(),
                                editTextLocation.text.toString(),
                                mLatitude,
                                mLongitude
                        )
                        val dbHandler = DatabaseHandler(this)
                        val addMyPlace = dbHandler.addHappyPlace(placeModel)    //if success it return positive value

                        //if success
                        if(addMyPlace>0){
                            Toast.makeText(applicationContext,"The place details have been inserted successfully.",Toast.LENGTH_LONG).show()
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun takePhotoFromCamera(){
        Dexter.withActivity(this).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        ).withListener(object: MultiplePermissionsListener{
            override fun onPermissionsChecked(report: MultiplePermissionsReport?){
                if(report!!.areAllPermissionsGranted()){
                    val galleryIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(galleryIntent, CAMERA)
                }
            }
            override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                showRationalDialogForPermissions()
            }
        }).onSameThread().check()
    }

    private fun choosePhotoFromGallery() {
        Dexter.withActivity(this).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(object: MultiplePermissionsListener{
            override fun onPermissionsChecked(report: MultiplePermissionsReport?){
                if(report!!.areAllPermissionsGranted()){
                    val galleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(galleryIntent, GALLERY)
                }
            }
            override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                showRationalDialogForPermissions()
            }
        }).onSameThread().check()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == GALLERY){
                if(data != null){
                    val contentURI = data.data
                    try{
                        val selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,contentURI)
                        saveImageToInternalStorage = saveImageToInternalStorage(selectedImageBitmap)
                        Log.e("Saved message","Path::$saveImageToInternalStorage")  ////to control in Logcat as error
                        imageViewPlaceImage.setImageBitmap(selectedImageBitmap)
                    }catch(e:IOException){
                        e.printStackTrace()
                        Toast.makeText(this@AddNewPlaceActivity,"Oops! Failed to load image from gallery.",Toast.LENGTH_SHORT).show()
                    }
                }
            }else if(requestCode == CAMERA){
                val thumbnail : Bitmap = data!!.extras!!.get("data") as Bitmap
                saveImageToInternalStorage = saveImageToInternalStorage(thumbnail)
                Log.e("Saved message","Path::$saveImageToInternalStorage") //to control in Logcat as error
                imageViewPlaceImage.setImageBitmap(thumbnail)
            }
        }
    }

    private fun saveImageToInternalStorage(bitmap:Bitmap):Uri{
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE) //other apps will not be able to access this directory(MODE_PRIVATE)
        file = File(file,"${UUID.randomUUID()}.jpg")
        try{
            val stream : OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
            stream.flush()
            stream.close()
        }catch(e:IOException){
            e.printStackTrace()
        }
        return Uri.parse(file.absolutePath)
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

    companion object{       //static variables
        private const val GALLERY = 1
        private const val CAMERA = 2
        private const val IMAGE_DIRECTORY = "MyPlacesImages"
    }
}