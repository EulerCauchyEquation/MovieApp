package com.hwonchul.movie.presentation.view.details;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hwonchul.movie.R;
import com.hwonchul.movie.databinding.ItemEmptyListBinding;
import com.hwonchul.movie.databinding.ItemPosterThumbnailBinding;
import com.hwonchul.movie.domain.model.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * 무비 포토 Adapter
 */
public class PosterThumbnailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // empty type
    private static final int VIEW_TYPE_EMPTY = 0;
    // item type
    private static final int VIEW_TYPE_ITEM = 1;

    private final List<Image> items = new ArrayList<>();

    public interface OnClickListener {
        void onClick(int currentPos);
    }

    private OnClickListener listener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // empty type
        if (viewType == VIEW_TYPE_EMPTY) {
            ItemEmptyListBinding binding = ItemEmptyListBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new EmptyViewHolder(binding);
        }
        // item type
        else if (viewType == VIEW_TYPE_ITEM) {
            ItemPosterThumbnailBinding binding = ItemPosterThumbnailBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new PosterThumbnailViewHolder(binding);
        }
        throw new IllegalArgumentException("invalid viewType = " + viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Empty ViewHolder
        if (holder instanceof EmptyViewHolder) {
            ((EmptyViewHolder) holder).bind();
        }
        // MoviePhoto ViewHolder
        else if (holder instanceof PosterThumbnailViewHolder) {
            final Image image = items.get(position);
            ((PosterThumbnailViewHolder) holder).bind(image);
        } else {
            throw new IllegalArgumentException("invalid viewHolder : " + holder.getClass());
        }
    }

    @Override
    public int getItemCount() {
        final int size = items.size();
        return size == 0 ? 1 : size;
    }

    @Override
    public int getItemViewType(int position) {
        return items.size() == 0 ? VIEW_TYPE_EMPTY : VIEW_TYPE_ITEM;
    }

    public void setItems(@NonNull List<Image> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    public void setOnClickListener(@NonNull OnClickListener listener) {
        this.listener = listener;
    }

    public class PosterThumbnailViewHolder extends RecyclerView.ViewHolder {
        private final ItemPosterThumbnailBinding binding;

        public PosterThumbnailViewHolder(@NonNull ItemPosterThumbnailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(@NonNull Image image) {
            binding.setItem(image);
            binding.executePendingBindings();

            binding.ivPoster.setOnClickListener(__ ->
                    listener.onClick(getBindingAdapterPosition()));
        }
    }

    /**
     * Empty View 표현하기 위한 ViewHolder
     */
    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        private final ItemEmptyListBinding binding;

        public EmptyViewHolder(@NonNull ItemEmptyListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind() {
            binding.tvEmptyList.setText(R.string.all_empty_photos);

            final int whiteColor = ContextCompat.getColor(
                    binding.getRoot().getContext(), R.color.WHITE);
            binding.tvEmptyList.setTextColor(whiteColor);
        }
    }
}
