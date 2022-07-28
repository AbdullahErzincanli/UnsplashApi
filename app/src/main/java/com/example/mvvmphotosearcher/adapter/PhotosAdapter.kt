package com.example.mvvmphotosearcher.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mvvmphotosearcher.R
import com.example.mvvmphotosearcher.model.UrlModels
import com.example.mvvmphotosearcher.view.PhotoScreenDirections
import kotlinx.android.synthetic.main.image_item_layout.view.*

class PhotosAdapter(val photos: ArrayList<UrlModels>): RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder>() {
    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.image_item_layout, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {

        Glide.with(holder.itemView).load(photos[0].results[position].urls.small)
            .into(holder.itemView.imageView)

        holder.itemView.setOnClickListener {
            val action = PhotoScreenDirections.actionPhotoScreenToBigPhoto()
            action.url = photos[0].results[position].urls.raw
            action.profilUrl =photos[0].results[position].user.links.html
            action.profilName = photos[0].results[position].user.firstName+" "+photos[0].results[position].user.lastName
            action.downloadUrl = photos[0].results[position].links.downloadLocation
            Navigation.findNavController(it).navigate(action)
        }

    }

    override fun getItemCount(): Int {
           return photos[0].results.size
    }

}
