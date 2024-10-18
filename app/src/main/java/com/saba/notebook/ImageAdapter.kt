package com.saba.notebook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ImageAdapter(
    private val imageList: MutableList<ByteArray>,
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

        // Load image using Glide from ByteArray to avoid UI blocking
        Glide.with(holder.imageView.context)
            .asBitmap()
            .load(imageByteArray)
            .into(holder.imageView)

        // Set click listener
        holder.imageView.setOnClickListener {
            onImageClick(imageByteArray)
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    fun addImage(imageByteArray: ByteArray) {
        imageList.add(imageByteArray)
        notifyItemInserted(imageList.size - 1)
    }
}
