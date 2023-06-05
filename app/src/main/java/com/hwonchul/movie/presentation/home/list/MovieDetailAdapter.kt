package com.hwonchul.movie.presentation.home.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hwonchul.movie.databinding.ItemMovieDetailBinding
import com.hwonchul.movie.domain.model.Movie

class MovieDetailAdapter : RecyclerView.Adapter<MovieDetailAdapter.MovieDetailViewHolder>() {

    private val items: MutableList<Movie> = ArrayList()

    interface OnClickListener {
        fun onClick(movie: Movie)
    }

    private lateinit var listener: OnClickListener

    fun setOnClickListener(listener: OnClickListener) {
        this.listener = listener
    }

    fun setItems(newItems: List<Movie>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieDetailViewHolder {
        val binding = ItemMovieDetailBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MovieDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieDetailViewHolder, position: Int) {
        val movie = items[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class MovieDetailViewHolder(
        private val binding: ItemMovieDetailBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.item = movie
            binding.layout.setOnClickListener { listener.onClick(movie) }
        }
    }
}