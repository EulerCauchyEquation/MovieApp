package com.hwonchul.movie.presentation.view.listing;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hwonchul.movie.databinding.ItemMovieBinding;
import com.hwonchul.movie.domain.model.Movie;

import java.util.ArrayList;
import java.util.List;


public class MovieAdapter extends
        RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private final List<Movie> items = new ArrayList<>();

    /**
     * 영화 상세 보기 클릭 리스너
     */
    public interface OnMovieDetailListener {
        /**
         * 상세 보기 클릭 콜백
         */
        void onClick(@NonNull Movie movie);
    }

    private OnMovieDetailListener listener;

    public void setOnMovieDetailListener(OnMovieDetailListener listener) {
        this.listener = listener;
    }

    public void setItems(@NonNull List<Movie> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMovieBinding binding = ItemMovieBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        final Movie movie = items.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        private final ItemMovieBinding binding;

        public MovieViewHolder(@NonNull ItemMovieBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Movie movie) {
            binding.setItem(movie);
            binding.setRank(getBindingAdapterPosition());

            binding.layoutPoster.setOnClickListener(__ -> listener.onClick(movie));
        }
    }
}
