package com.hwonchul.movie.presentation.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hwonchul.movie.R
import com.hwonchul.movie.databinding.ItemEmptyListBinding
import com.hwonchul.movie.databinding.ItemPosterThumbnailBinding
import com.hwonchul.movie.domain.model.Image

/**
 * 무비 포토 Adapter
 */
class PosterThumbnailAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items: MutableList<Image> = ArrayList()

    interface OnClickListener {
        fun onClick(currentPos: Int)
    }

    private lateinit var listener: OnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            VIEW_TYPE_EMPTY -> {
                val binding = ItemEmptyListBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                return EmptyViewHolder(binding)
            }

            VIEW_TYPE_ITEM -> {
                val binding = ItemPosterThumbnailBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                return PosterThumbnailViewHolder(binding)
            }

            else -> throw IllegalArgumentException("invalid viewType = $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is EmptyViewHolder -> holder.bind()
            is PosterThumbnailViewHolder -> {
                val image = items[position]
                holder.bind(image)
            }

            else -> throw IllegalArgumentException("invalid viewHolder : " + holder.javaClass)
        }
    }

    override fun getItemCount(): Int {
        val size = items.size
        return if (size == 0) 1 else size
    }

    override fun getItemViewType(position: Int): Int {
        return if (items.size == 0) VIEW_TYPE_EMPTY else VIEW_TYPE_ITEM
    }

    fun setItems(newItems: List<Image>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun setOnClickListener(listener: OnClickListener) {
        this.listener = listener
    }

    inner class PosterThumbnailViewHolder(
        private val binding: ItemPosterThumbnailBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(image: Image) {
            binding.item = image
            binding.executePendingBindings()
            binding.ivPoster.setOnClickListener { listener.onClick(bindingAdapterPosition) }
        }
    }

    inner class EmptyViewHolder(
        private val binding: ItemEmptyListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.tvEmptyList.setText(R.string.all_empty_photos)
            val whiteColor = ContextCompat.getColor(
                binding.root.context, R.color.WHITE
            )
            binding.tvEmptyList.setTextColor(whiteColor)
        }
    }

    companion object {
        // empty type
        private const val VIEW_TYPE_EMPTY = 0

        // item type
        private const val VIEW_TYPE_ITEM = 1
    }
}