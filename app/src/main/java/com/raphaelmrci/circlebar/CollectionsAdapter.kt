package com.raphaelmrci.circlebar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raphaelmrci.circlebar.models.Cocktail

class CollectionsAdapter(private val mCollections : MutableMap<String, MutableList<Cocktail>>, private val mContext: Context) : RecyclerView.Adapter<CollectionsAdapter.ViewHolder>() {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        val collectionTitle: TextView = itemView.findViewById(R.id.collectionTitle)
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
    }
}