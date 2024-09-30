package com.saba.notebook

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.saba.notebook.R

class ImageAdapter(
    private val imageList: List<ByteArray>,
    private val onImageClick: (ByteArray) -> Unit
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageByteArray = imageList[position]

        // Load image using Glide
        Glide.with(holder.itemView.context)
            .asBitmap()  // Load image as Bitmap
            .load(imageByteArray)  // Load image from byte array
            .placeholder(R.drawable.placeholder_image)  // Placeholder while loading
            .error(R.drawable.placeholder_image)  // Error placeholder in case of failure
            .into(holder.imageView)

        // Set click listener
        holder.imageView.setOnClickListener {
            onImageClick(imageByteArray)
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }
}
