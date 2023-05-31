package com.raphaelmrci.circlebar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.raphaelmrci.circlebar.network.SocketHandler
import com.squareup.picasso.Picasso

class CocktailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cocktail) 

        val title = intent.getStringExtra("cocktailName")
        val imageURL = intent.getStringExtra("imageURL")
        val cocktailID = intent.getIntExtra("cocktailID", -1)
        val description = intent.getStringExtra("cocktailDescription")

        Log.d("DEBUG", cocktailID.toString())

        val titleTextView = findViewById<TextView>(R.id.titleText)
        val descriptionTextView = findViewById<TextView>(R.id.description)
        val fabBack = findViewById<FloatingActionButton>(R.id.returnButton)
        val thumbnail = findViewById<ImageView>(R.id.thumbnail)
        val commandBtn = findViewById<Button>(R.id.commandBtn)

        val popup = findViewById<LinearLayout>(R.id.popup)
        val cancelPopup = findViewById<Button>(R.id.cancelPopup)
        val confirmPopup = findViewById<Button>(R.id.confirmPopup)

        commandBtn.setOnClickListener {
            popup.visibility = View.VISIBLE
        }

        cancelPopup.setOnClickListener {
            popup.visibility = View.GONE
        }

        confirmPopup.setOnClickListener {
            val intent = Intent(this@CocktailActivity, WaitingActivity::class.java)
            intent.putExtra("cocktailID", cocktailID)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(intent)
            finish()
        }


        titleTextView.text = title
        Picasso.get().load(imageURL).into(thumbnail)
        descriptionTextView.text = description

        fabBack.setOnClickListener {
            finish()
        }
    }
}