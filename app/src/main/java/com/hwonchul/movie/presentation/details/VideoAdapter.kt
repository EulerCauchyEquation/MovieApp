package com.hwonchul.movie.presentation.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hwonchul.movie.R
import com.hwonchul.movie.databinding.ItemVideoBinding
import com.hwonchul.movie.databinding.LayoutDataEmptyBinding
import com.hwonchul.movie.domain.model.Video

/**
 * 영화 클립 adapter
 */
class VideoAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items: MutableList<Video> = ArrayList()

    interface OnClickListener {
        fun onClick(url: String)
    }

    private lateinit var listener: OnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            VIEW_TYPE_EMPTY -> {
                val binding = LayoutDataEmptyBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                return EmptyViewHolder(binding)
            }

            VIEW_TYPE_ITEM -> {
                val binding = ItemVideoBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                return VideoViewHolder(binding)
            }

            else -> throw IllegalArgumentException("invalid viewType = $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is EmptyViewHolder -> holder.bind()
            is VideoViewHolder -> {
                val video = items[position]
                holder.bind(video)
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

    fun setOnClickListener(listener: OnClickListener) {
        this.listener = listener
    }

    fun setItems(newItems: List<Video>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class VideoViewHolder(
        private val binding: ItemVideoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(video: Video) {
            binding.item = video
            binding.executePendingBindings()
            binding.layoutYoutube.setOnClickListener { listener.onClick(video.videoUrl) }
        }
    }

    inner class EmptyViewHolder(
        private val binding: LayoutDataEmptyBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.tvEmpty.setText(R.string.all_empty_videos)
            binding.imageview.visibility = View.GONE
        }
    }

    companion object {
        // empty type
        private const val VIEW_TYPE_EMPTY = 0

        // item type
        private const val VIEW_TYPE_ITEM = 1
    }
}