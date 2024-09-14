package com.saba.notebook

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ImageAdapter(
    private val imageList: List<ByteArray>,
    private val onImageClick: (ByteArray) -> Unit
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView1: ImageView = view.findViewById(R.id.imageView1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageByteArray1 = imageList.getOrNull(position * 2)

        holder.imageView1.setOnClickListener {
            imageByteArray1?.let { onImageClick(it) }
        }


        imageByteArray1?.let {
            val bitmap1 = BitmapFactory.decodeByteArray(it, 0, it.size)
            holder.imageView1.setImageBitmap(bitmap1)
        }

    }

    override fun getItemCount(): Int {
        return (imageList.size + 1) / 2
    }
}
