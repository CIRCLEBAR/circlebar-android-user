package com.raphaelmrci.circlebar

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raphaelmrci.circlebar.models.Cocktail

class CollectionsAdapter(private val mCollections : MutableMap<String, MutableList<Cocktail>>, private val mContext: Context) : RecyclerView.Adapter<CollectionsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val collectionTitle: TextView = itemView.findViewById(R.id.collectionTitle)
        var showAllCollection: ImageView = itemView.findViewById(R.id.showAllArrow)
        val cocktailsRecycler: RecyclerView = itemView.findViewById(R.id.cocktailsList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val collectionView = inflater.inflate(R.layout.home_collection_item, parent, false)

        return ViewHolder(collectionView)
    }

    override fun getItemCount(): Int {
        return mCollections.size
    }

    override fun onBindViewHolder(holder: CollectionsAdapter.ViewHolder, position: Int) {
        val collectionName = mCollections.keys.elementAt(position)
        val collectionTitle = holder.collectionTitle
        val adapter = mCollections[collectionName]?.let { CocktailAdapter(it, mContext) }

        holder.cocktailsRecycler.adapter = adapter
        holder.cocktailsRecycler.layoutManager = LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
        collectionTitle.text = collectionName

        holder.showAllCollection.setOnClickListener {
            val intent = Intent(mContext, CollectionActivity::class.java)
            intent.putExtra("collectionName", collectionName)
            mContext.startActivity(intent)
        }
    }
}