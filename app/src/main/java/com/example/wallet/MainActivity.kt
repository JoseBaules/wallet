package com.example.wallet

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val REQUEST_PERMISSIONS =1
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_PERMISSIONS)

        setContentView(R.layout.activity_main_menu)

        val recyclerView: RecyclerView = findViewById(R.id.rv_documents)
        recyclerView.layoutManager = GridLayoutManager(this,2)
//        recyclerView.adapter = DocumentAdapter(get.)

        val fabAddDocument: FloatingActionButton =  findViewById(R.id.fab_add_document)

        fabAddDocument.setOnClickListener{

            //Intent to call addDocument
            val intent = Intent(this, AddDocumentActivity::class.java)
            startActivity(intent)


        }

    }
}
