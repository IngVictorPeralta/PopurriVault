package com.vhpg.popurrivault.util

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.vhpg.popurrivault.application.PopurriVaultBDApp
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CameraHelper(private val context: Context) {

    private val REQUEST_CAMERA_PERMISSION = 100
    private val REQUEST_IMAGE_CAPTURE = 101
    private var imageUri: Uri? = null

    fun openCamera(fragment: Fragment) {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "MyPicture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on ${System.currentTimeMillis()}")
        imageUri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

        //cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_BACK)

        // Configurar la relación de aspecto
        //cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1) // Cámara trasera (0 para la cámara frontal)
        //cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1) // Cámara trasera (0 para la cámara frontal)
        //cameraIntent.putExtra("android.intent.extra.USE_FRONT_CAMERA", false)

        // Establecer la relación de aspecto deseada (por ejemplo, 1:1 o 3:4)
        //cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1) // Cámara trasera (0 para la cámara frontal)
        cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1) // Cámara trasera (0 para la cámara frontal)
        cameraIntent.putExtra("android.intent.extra.USE_FRONT_CAMERA", false)
        cameraIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1) // Cámara trasera (0 para la cámara frontal)
        cameraIntent.putExtra("android.intent.extras.LENS_FACING_BACK", 0) // Cámara trasera (0 para la cámara frontal)
        cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1) // Cámara trasera (0 para la cámara frontal)
        cameraIntent.putExtra("android.intent.extras.LENS_FACING_BACK", 0) // Cámara trasera (0 para la cámara frontal)


        fragment.startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
    }

    fun getImageUri(): Uri? {
        return imageUri
    }


}


/*class CameraHelper(private val context: Context) {
    companion object {
        const val CAMERA_PERMISSION_CODE = 101
        const val CAMERA_REQUEST_CODE = 102
    }

    private var cameraUri: Uri? = null

    fun requestCameraPermission(fragment: Fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                fragment.requestPermissions(
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_CODE
                )
            } else {
                openCamera(fragment)
            }
        } else {
            openCamera(fragment)
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        cameraHelper = CameraHelper(requireContext())
        cameraHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Aquí puedes utilizar la URI de la imagen capturada (cameraUri)
            // por ejemplo, para mostrar la imagen en un ImageView
            // imageView.setImageURI(cameraUri)
        }
    }

    private fun openCamera(fragment: Fragment) {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraUri = createImageUri(fragment.requireContext())
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri)
        fragment.startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }


    private fun createImageUri(context: Context): Uri {
        val contentResolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        }
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
    }
}*/

/*class CameraHelper() {

    private lateinit var launcher: ActivityResultLauncher<Intent>

    init {
        initializeLauncher()
    }

    private fun initializeLauncher() {
        launcher = lifecycleOwner.(
            ActivityResultContracts.startActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as Bitmap
                // Puedes manejar la imagen resultante aquí
                saveImageToExternalStorage(imageBitmap)
            }
        }
    }

    fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(android.Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        } else {
            dispatchTakePictureIntent()
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
            launcher.launch(takePictureIntent)
        }
    }

    fun saveImageToExternalStorage(bitmap: Bitmap) {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!

        try {
            val imageFile = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
            )
            val fos = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.close()
            // La imagen se guarda en imageFile
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 200
    }
}*/