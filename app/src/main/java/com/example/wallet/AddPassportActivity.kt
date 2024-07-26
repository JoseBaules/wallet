package com.example.wallet

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Response
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException


class AddPassportActivity : AppCompatActivity() {

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_PERMISSIONS = 2

    private lateinit var ivPassportPhoto: ImageView
    private lateinit var photoFile: File
    private lateinit var photoUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_passport)

        val llAddPassport: LinearLayout = findViewById(R.id.ll_add_passport)
        ivPassportPhoto = findViewById(R.id.passportPhoto)

        llAddPassport.setOnClickListener {
//
//            // Intent to open the image picker
//            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            startActivityForResult(intent, 100)


            //check permissions

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "No Permission", Toast.LENGTH_SHORT).show()

            } else {
                // Start camera intent
                dispatchTakePictureIntent()


            }

        }
    }

    private fun dispatchTakePictureIntent() {

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (takePictureIntent.resolveActivity(packageManager) != null)
        {
            photoFile = createImageFile()

            photoUri = FileProvider.getUriForFile(this,"com.example.wallet.fileprovider",photoFile)

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri)

            startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE)
        }

    }

    private fun createImageFile(): File {

        val storageDir : File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!

        return File.createTempFile("passport_photo",".jpg",storageDir)
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {

            ivPassportPhoto.setImageURI(photoUri)

            val bitMap = BitmapFactory.decodeFile(photoFile.absolutePath)

            performOCR(bitMap)
        }
    }

    private fun performOCR(bitmap: Bitmap) {

        val url = "${this.getString(R.string.endpoint_azure)}/vision/v3.1/ocr?language=unk&detectOrientation=true"


        val outputStream = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream)

        val byteArray = outputStream.toByteArray()

        val client = okhttp3.OkHttpClient()

        val requestBody = okhttp3.RequestBody.create("application/octet-stream".toMediaTypeOrNull(),byteArray)

        val request = okhttp3.Request
            .Builder()
            .url(url)
            .addHeader("Ocp-Apim-Subscription-Key" , this.getString(R.string.subscriptionKey))
            .post(requestBody)
            .build()


        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {

                runOnUiThread {

                    println("Not Connected to Azure")

                    Toast.makeText(this@AddPassportActivity,"Error Connecting To Microsoft Azure",Toast.LENGTH_SHORT).show()
                }
            }


            override fun onResponse(call: Call, response: Response) {

                if (response.isSuccessful)
                {
                    val jsonResponse = response.body?.string()
                    val jsonObject = JSONObject(jsonResponse?:"")
                    val resultText = parseOCRResponse(jsonObject)

                    println("Connected to Azure $resultText")
                    
                    runOnUiThread {

                        Toast.makeText(this@AddPassportActivity,"Successfully Connected to Azure",Toast.LENGTH_SHORT).show()

                    }
                }
                else
                {
                    println("Not Connected to Azure")

                    kotlin.run {
                        Toast.makeText(this@AddPassportActivity,"Error Connecting To Microsoft Azure",Toast.LENGTH_SHORT).show()
                    }
                }

            }
        })

    }

    private fun parseOCRResponse(jsonObject: JSONObject) {

        val regions = jsonObject.getJSONArray("regions")?: return
        val passportData = StringBuilder()

        for (i in 0 until regions.length()) {
            val region = regions.getJSONObject(i)
            val lines = region.getJSONArray("lines") ?: continue
            for (j in 0 until lines.length()) {
                val line = lines.getJSONObject(j)
                val words = line.getJSONArray("words") ?: continue
                for (k in 0 until words.length()) {
                    val word = words.getJSONObject(k)
                    passportData.append(word.getString("text")).append(" ")
                }
            }
        }
        passportData.append("\n")

        println("Azure OCR Response: $passportData")

    }


}
