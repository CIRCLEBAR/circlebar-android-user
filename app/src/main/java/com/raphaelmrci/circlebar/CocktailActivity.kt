package com.raphaelmrci.circlebar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso

class CocktailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cocktail) 

        val title = intent.getStringExtra("cocktailName")
        val imageURL = intent.getStringExtra("imageURL")
        val cocktailID = intent.getStringExtra("cocktailId")
        val description = intent.getStringExtra("cocktailDescription")

        val titleTextView = findViewById<TextView>(R.id.titleText)
        val descriptionTextView = findViewById<TextView>(R.id.description)
        val fabBack = findViewById<FloatingActionButton>(R.id.returnButton)
        val thumbnail = findViewById<ImageView>(R.id.thumbnail)

        titleTextView.text = title
        Picasso.get().load(imageURL).into(thumbnail)
        descriptionTextView.text = description

        fabBack.setOnClickListener {
            finish()
        }
    }
}