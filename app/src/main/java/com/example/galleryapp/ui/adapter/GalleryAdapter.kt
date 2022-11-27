package com.example.galleryapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.galleryapp.databinding.ItemDateBinding
import com.example.galleryapp.databinding.ItemPhotoBinding

class GalleryAdapter(
    private var mItems: List<GalleryObject> = emptyList(),
    private val listener: ((GalleryObject.PhotoItem) -> Unit)
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val PHOTO_TYPE = 111
        private const val DATE_TYPE = 222
    }

    sealed class GalleryObject {
        class PhotoItem(val url: String, val folderName: String) : GalleryObject()
        class DateItem(val time: String) : GalleryObject()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(items: List<GalleryObject>) {
        mItems = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            PHOTO_TYPE -> {
                val binding =
                    ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                PhotoViewHolder(listener, binding)
            }
            else -> {
                val binding =
                    ItemDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                DateViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PhotoViewHolder -> holder.onBind(mItems[position] as GalleryObject.PhotoItem)
            is DateViewHolder -> holder.onBind(mItems[position] as GalleryObject.DateItem)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (mItems[position]) {
            is GalleryObject.PhotoItem -> PHOTO_TYPE
            else -> DATE_TYPE
        }
    }

    override fun getItemCount() = mItems.size

    fun getSpanCount(pos: Int): Int {
        return when (mItems[pos]) {
            is GalleryObject.PhotoItem -> 1
            else -> 4
        }
    }

    class PhotoViewHolder(listener: (GalleryObject.PhotoItem) -> Unit, private val binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.ivPhoto.setOnClickListener {
                (binding.root.tag as? GalleryObject.PhotoItem)?.apply {
                    listener.invoke(this)
                }
            }
        }

        fun onBind(item: GalleryObject.PhotoItem) = with(binding) {
            root.tag = item
            Glide.with(ivPhoto.context).load(item.url).into(ivPhoto)
        }
    }

    class DateViewHolder(private val binding: ItemDateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: GalleryObject.DateItem) = with(binding) {
            tvDate.text = item.time
        }
    }
}


