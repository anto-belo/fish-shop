package com.ab.lab.ui.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ab.lab.R
import com.ab.lab.core.extension.showExt
import com.ab.lab.db.entity.Favorites

class FavoritesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val productImage: ImageView = view.findViewById(R.id.productImage)
    private val nameView: TextView = view.findViewById(R.id.nameView)
    private val descriptionView: TextView = view.findViewById(R.id.descriptionView)

    fun bind(favorites: Favorites?) {
        favorites?.let {
            showData(it)
            showImage(it)
        }
    }

    private fun showData(favorites: Favorites) {
        nameView.text = favorites.name
        descriptionView.text = favorites.description
    }

    private fun showImage(favorites: Favorites) {
        val imageUrl = favorites.imageUrl
        if (imageUrl != null && imageUrl.isNotEmpty()) {
            productImage.showExt("https://$imageUrl")
        } else {
            productImage.setImageResource(R.drawable.ic_image_24)
        }
    }

    companion object {
        fun create(parent: ViewGroup): FavoritesViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.favorites_view_item, parent, false)
            return FavoritesViewHolder(view)
        }
    }
}
