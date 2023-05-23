package com.raphaelmrci.circlebar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.raphaelmrci.circlebar.models.Cocktail
import com.raphaelmrci.circlebar.network.ApiClient
import com.squareup.picasso.Picasso

class CocktailAdapter(private val mCocktails : MutableList<Cocktail>) : RecyclerView.Adapter<CocktailAdapter.ViewHolder>() {
    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.cocktailCardName)
        val thumbnail: ImageView = itemView.findViewById(R.id.thumbnailCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val cardView = inflater.inflate(R.layout.cocktail_card, parent, false)
        return ViewHolder(cardView)
    }

    override fun getItemCount(): Int {
        return mCocktails.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cocktailName = mCocktails[position].name
        val cocktailImage = ApiClient.BASE_URL + mCocktails[position].image

        Picasso.get().load(cocktailImage).into(holder.thumbnail)
        holder.titleText.text = cocktailName
    }
}