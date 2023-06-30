package com.raphaelmrci.circlebar

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raphaelmrci.circlebar.models.Cocktail
import com.raphaelmrci.circlebar.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CollectionActivity : AppCompatActivity(), CoroutineScope {

    private var collection = mutableListOf<Cocktail>()

    private lateinit var recyclerView : RecyclerView
    private lateinit var recyclerAdapter : CocktailAdapter
    private var collectionName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection)

        collectionName = intent.getStringExtra("collectionName")

        val collectionTitle = findViewById<TextView>(R.id.collectionName)
        val backArrow = findViewById<ImageView>(R.id.backArrow)

        recyclerView = findViewById(R.id.collectionList)
        recyclerAdapter = CocktailAdapter(collection, this@CollectionActivity)
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        collectionTitle.text = collectionName

        backArrow.setOnClickListener {
            onBackPressed()
        }
        executeCall()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun executeCall() {
        launch(Dispatchers.Main) {
            try {
                val response = ApiClient.apiService.getAvailableCocktails()
                val collections = mutableMapOf<String, MutableList<Cocktail>>()

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()

                    Log.d("GET", content.toString())

                    if (content != null) {
                        var count = 0
                        content.forEach { cocktail ->
                            cocktail.collections.forEach { collName ->
                                if (collections[collName] == null) {
                                    collections[collName] = mutableListOf()
                                }
                                collections[collName]?.add(cocktail)
                            }
                            count++
                        }
                        collection.clear()
                        collections[collectionName]?.let { collection.addAll(it) }
                        recyclerView.adapter?.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(
                        this@CollectionActivity,
                        "Error Occurred: ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {
                Toast.makeText(
                    this@CollectionActivity,
                    "Error Occurred: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Job()
}