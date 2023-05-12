package com.hwonchul.movie.presentation.poster

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hwonchul.movie.databinding.ItemPosterBinding
import com.hwonchul.movie.domain.model.Image

/**
 * 사진 상세 화면 adapter
 */
class PosterAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items: MutableList<Image> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemPosterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PosterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PosterViewHolder) {
            val image = items[position]
            holder.bind(image)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(newItems: List<Image>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class PosterViewHolder(
        private val binding: ItemPosterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(image: Image) {
            binding.item = image
            binding.executePendingBindings()
        }
    }
}