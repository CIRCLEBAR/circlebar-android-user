package com.raphaelmrci.circlebar

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.raphaelmrci.circlebar.models.Cocktail
import com.raphaelmrci.circlebar.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class HomeActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var fadeinAnim : Animation
    private lateinit var noCocktailsText : TextView
    private lateinit var refreshLayout : SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView


    private var collections = mutableMapOf<String, MutableList<Cocktail>>()

    private lateinit var recyclerAdapter : CollectionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        fadeinAnim = AnimationUtils.loadAnimation(this@HomeActivity, R.anim.fadein)

        recyclerAdapter = CollectionsAdapter(collections, this@HomeActivity)

        refreshLayout = findViewById(R.id.refreshHomeLayout)
        noCocktailsText = findViewById(R.id.noCocktailsText)
        recyclerView = findViewById(R.id.collectionsList)

        refreshLayout.setOnRefreshListener {
            noCocktailsText.visibility = View.GONE
            collections.clear()
            executeCall()
        }

        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        executeCall()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun executeCall() {
        launch(Dispatchers.Main) {
            try {
                val response = ApiClient.apiService.getAvailableCocktails()

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()

                    Log.d("GET", content.toString())

                    refreshLayout.isRefreshing = false

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
                        recyclerView.adapter!!.notifyDataSetChanged()
                        if (count == 0) {
                            noCocktailsText.startAnimation(fadeinAnim)
                            noCocktailsText.visibility = View.VISIBLE
                        }
                    } else {
                        noCocktailsText.startAnimation(fadeinAnim)
                        noCocktailsText.visibility = View.VISIBLE
                    }
                } else {
                    Toast.makeText(
                        this@HomeActivity,
                        "Error Occurred: ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {
                Toast.makeText(
                    this@HomeActivity,
                    "Error Occurred: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Job()
}
