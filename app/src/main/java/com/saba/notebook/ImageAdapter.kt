package com.saba.notebook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

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

        // Load the image using Glide directly from the ByteArray
        Glide.with(holder.itemView.context)
            .asBitmap()  // Ensure it's treated as a Bitmap for ImageView
            .load(imageByteArray)  // Load image from ByteArray
            .placeholder(R.drawable.placeholder_image)  // Placeholder while loading
            .error(R.drawable.placeholder_image)  // Error placeholder
            .into(holder.imageView)

        // Set click listener on the image
        holder.imageView.setOnClickListener {
            onImageClick(imageByteArray)
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }
}
