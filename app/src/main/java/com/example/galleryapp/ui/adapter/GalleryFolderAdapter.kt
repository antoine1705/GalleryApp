package com.example.galleryapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.galleryapp.R
import com.example.galleryapp.databinding.ItemPhotoFolderBinding
import com.example.galleryapp.model.PhotoFolder

class GalleryFolderAdapter(
    private var mItems: List<PhotoFolder> = emptyList(),
    private val listener: ((String) -> Unit)
) :
    RecyclerView.Adapter<GalleryFolderAdapter.ImageFolderViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(items: List<PhotoFolder>) {
        mItems = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageFolderViewHolder {
        val binding =
            ItemPhotoFolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageFolderViewHolder(listener, binding)
    }

    override fun onBindViewHolder(holder: ImageFolderViewHolder, position: Int) {
        holder.onBind(mItems[position])
    }

    override fun getItemCount() = mItems.size

    class ImageFolderViewHolder(
        listener: (String) -> Unit,
        private val binding: ItemPhotoFolderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.ivFolderPhoto.setOnClickListener {
                listener.invoke(binding.root.tag.toString())
            }
        }

        fun onBind(item: PhotoFolder) = with(binding) {
            root.tag = item.folderName
            tvFolderName.text = item.folderName
            tvPhotoCount.text = root.context.getString(R.string.photo_count, item.photos.size)
            Glide.with(ivFolderPhoto.context).load(item.photos.firstOrNull()?.url).into(ivFolderPhoto)
        }
    }
}


