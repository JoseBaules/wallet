package com.example.wallet
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddDocumentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_document)

        val paymentCardLayout: LinearLayout = findViewById(R.id.ll_payment_card)
        val passportLayout: LinearLayout = findViewById(R.id.ll_passport)
        val idLayout: LinearLayout = findViewById(R.id.ll_id)
        val transitLayout: LinearLayout = findViewById(R.id.ll_transit)

        paymentCardLayout.setOnClickListener {
            // Handle click event
            Toast.makeText(this, "Payment Card Clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,AddPaymentCardActivity::class.java)
            startActivity(intent)
        }

        passportLayout.setOnClickListener {
            // Handle click event
            Toast.makeText(this, "Passport Clicked", Toast.LENGTH_SHORT).show()
            //Intent to call addDocument
            val intent = Intent(this, AddPassportActivity::class.java)
            startActivity(intent)
        }

        idLayout.setOnClickListener {
            // Handle click event
            Toast.makeText(this, "Identification Clicked", Toast.LENGTH_SHORT).show()
            //Intent to call addDocument
            val intent = Intent(this, AddIdentificationActivity::class.java)
            startActivity(intent)
        }

        transitLayout.setOnClickListener {
            // Handle click event
            Toast.makeText(this, "transit pass Clicked", Toast.LENGTH_SHORT).show()
            //Intent to call transitL
            val intent = Intent(this, AddTransitPassActivity::class.java)
            startActivity(intent)
        }

    }
}