package com.hwonchul.movie.presentation.view.poster;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hwonchul.movie.databinding.ItemPosterBinding;
import com.hwonchul.movie.domain.model.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * 사진 상세 화면 adapter
 */
public class PosterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<Image> items = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPosterBinding binding = ItemPosterBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new PosterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PosterViewHolder) {
            final Image image = items.get(position);
            ((PosterViewHolder) holder).bind(image);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(@NonNull List<Image> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    public class PosterViewHolder extends RecyclerView.ViewHolder {
        private final ItemPosterBinding binding;

        public PosterViewHolder(@NonNull ItemPosterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(@NonNull Image image) {
            binding.setItem(image);
            binding.executePendingBindings();
        }
    }
}
