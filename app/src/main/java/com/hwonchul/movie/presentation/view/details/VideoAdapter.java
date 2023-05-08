package com.hwonchul.movie.presentation.view.details;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hwonchul.movie.R;
import com.hwonchul.movie.databinding.ItemEmptyListBinding;
import com.hwonchul.movie.databinding.ItemVideoBinding;
import com.hwonchul.movie.domain.model.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * 영화 클립 adapter
 */
public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // empty type
    private static final int VIEW_TYPE_EMPTY = 0;
    // item type
    private static final int VIEW_TYPE_ITEM = 1;
    // items
    private final List<Video> items = new ArrayList<>();

    public interface OnClickListener {
        void onClick(String url);
    }

    private OnClickListener listener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // empty viewHolder
        if (viewType == VIEW_TYPE_EMPTY) {
            ItemEmptyListBinding binding = ItemEmptyListBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new EmptyViewHolder(binding);
        }
        // item viewHolder
        else if (viewType == VIEW_TYPE_ITEM) {
            ItemVideoBinding binding = ItemVideoBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new VideoViewHolder(binding);
        }
        throw new IllegalArgumentException("invalid viewType = " + viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Empty ViewHolder
        if (holder instanceof EmptyViewHolder) {
            ((EmptyViewHolder) holder).bind();
        }
        // MovieVideo ViewHolder
        else if (holder instanceof VideoViewHolder) {
            final Video video = items.get(position);
            ((VideoViewHolder) holder).bind(video);
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

    public void setOnClickListener(@NonNull OnClickListener listener) {
        this.listener = listener;
    }

    public void setItems(@NonNull List<Video> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        private final ItemVideoBinding binding;

        public VideoViewHolder(@NonNull ItemVideoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(@NonNull Video video) {
            binding.setItem(video);
            binding.executePendingBindings();
            binding.layoutYoutube.setOnClickListener(__ -> listener.onClick(video.getVideoUrl()));
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
            binding.tvEmptyList.setText(R.string.all_empty_videos);

            final int whiteColor = ContextCompat.getColor(
                    binding.getRoot().getContext(), R.color.WHITE);
            binding.tvEmptyList.setTextColor(whiteColor);
        }
    }
}