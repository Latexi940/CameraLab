package com.example.cameralab

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    private val REQUEST_IMAGE_CAPTURE = 1
    private var imageFile: File? = null
    private var mCurrentPhotoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.i("CAMLAB", "Starting app")

        val fileName = "temp_photo"
        val imgPath = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        imageFile = File.createTempFile(fileName, ".jpg", imgPath)

        mCurrentPhotoPath = imageFile!!.absolutePath

        Log.i("CAMLAB", "Photos will be saved in $mCurrentPhotoPath")

        val photoURI: Uri = FileProvider.getUriForFile(
            this,
            "com.example.cameralab.fileprovider",
            imageFile!!
        )

        cameraButton.setOnClickListener {
            Log.i("CAMLAB", "Camera button pressed")
            val myIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (myIntent.resolveActivity(packageManager) != null) {
                myIntent.flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
                myIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(myIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, recIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, recIntent)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Log.i("CAMLAB", "Picture is good")
            val imageBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath)
            imageView.setImageBitmap(imageBitmap)
        }
    }
}