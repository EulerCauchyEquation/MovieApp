package com.hwonchul.movie.presentation.home.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hwonchul.movie.R
import com.hwonchul.movie.databinding.ItemMovieDetailBinding
import com.hwonchul.movie.domain.model.Movie

class MovieDetailAdapter :
    PagingDataAdapter<Movie, MovieDetailAdapter.MovieDetailViewHolder>(DIFF_CALLBACK) {

    interface MovieClickListener {
        fun onClick(movie: Movie)
    }

    private lateinit var movieClickListener: MovieClickListener

    fun setMovieClickListener(movieClickListener: MovieClickListener) {
        this.movieClickListener = movieClickListener
    }

    interface FavoritesClickListener {
        fun onClick(movie: Movie, isFavorite: Boolean)
    }

    private lateinit var favoritesClickListener: FavoritesClickListener

    fun setFavoritesClickListener(favoritesClickListener: FavoritesClickListener) {
        this.favoritesClickListener = favoritesClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieDetailViewHolder {
        val binding = ItemMovieDetailBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MovieDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieDetailViewHolder, position: Int) {
        val movie = getItem(position) ?: return
        holder.bind(movie)
    }

    inner class MovieDetailViewHolder(
        private val binding: ItemMovieDetailBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.item = movie

            if (movie.isFavorite) {
                binding.btnFavorites.setImageResource(R.drawable.ic_favorites)
            } else {
                binding.btnFavorites.setImageResource(R.drawable.ic_unfavorites)
            }
            binding.btnFavorites.setOnClickListener {
                favoritesClickListener.onClick(
                    movie,
                    movie.isFavorite
                )
            }

            binding.layout.setOnClickListener { movieClickListener.onClick(movie) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                // id가 같으면 같은 아이템으로 판단
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                // 내용이 같은지 비교
                return oldItem == newItem
            }
        }
    }
}