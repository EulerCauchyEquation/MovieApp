package com.hwonchul.movie.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hwonchul.movie.databinding.ItemMovieBinding
import com.hwonchul.movie.domain.model.Movie
import com.hwonchul.movie.presentation.home.MovieAdapter.MovieViewHolder

class MovieAdapter : RecyclerView.Adapter<MovieViewHolder>() {

    private val items: MutableList<Movie> = ArrayList()

    interface OnMovieDetailListener {
        fun onClick(movie: Movie)
    }

    private lateinit var listener: OnMovieDetailListener
    private var limit = Integer.MAX_VALUE

    fun setOnMovieDetailListener(listener: OnMovieDetailListener) {
        this.listener = listener
    }

    fun setItems(newItems: List<Movie>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun setLimit(limit: Int) {
        this.limit = limit
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = items[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return Math.min(items.size, limit)
    }

    inner class MovieViewHolder(
        private val binding: ItemMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.item = movie
            binding.rank = bindingAdapterPosition
            binding.layoutPoster.setOnClickListener { listener.onClick(movie) }
        }
    }
}