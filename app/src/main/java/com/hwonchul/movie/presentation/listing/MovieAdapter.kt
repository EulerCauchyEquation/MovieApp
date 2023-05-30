package com.hwonchul.movie.presentation.listing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hwonchul.movie.databinding.ItemMovieBinding
import com.hwonchul.movie.domain.model.Movie
import com.hwonchul.movie.presentation.listing.MovieAdapter.MovieViewHolder

class MovieAdapter : RecyclerView.Adapter<MovieViewHolder>() {

    private val items: MutableList<Movie> = ArrayList()

    interface OnMovieDetailListener {
        fun onClick(movie: Movie)
    }

    private lateinit var listener: OnMovieDetailListener

    fun setOnMovieDetailListener(listener: OnMovieDetailListener) {
        this.listener = listener
    }

    fun setItems(newItems: List<Movie>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
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
        return items.size
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