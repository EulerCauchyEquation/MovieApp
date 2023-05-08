package com.hwonchul.movie.util;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.hwonchul.movie.R;
import com.hwonchul.movie.domain.model.Movie;
import com.hwonchul.movie.domain.model.Image;
import com.hwonchul.movie.domain.model.Video;
import com.hwonchul.movie.presentation.view.details.PosterThumbnailAdapter;
import com.hwonchul.movie.presentation.view.details.VideoAdapter;
import com.hwonchul.movie.presentation.view.listing.MovieAdapter;
import com.hwonchul.movie.presentation.view.poster.PosterAdapter;

import java.time.LocalDate;
import java.util.List;

import timber.log.Timber;

public class BindingUtils {

    @BindingAdapter(value = {"imageUrl", "placeholder", "fallback"}, requireAll = false)
    public static void bindImage(@NonNull ImageView view,
                                 @NonNull String url,
                                 @Nullable Drawable placeholder,
                                 @Nullable Drawable fallback) {
        Glide.with(view.getContext())
                .load(url)
                // key를 하루 단위로 변경한다.
                .signature(new ObjectKey(LocalDate.now().toEpochDay()))
                .placeholder(placeholder)
                .fallback(fallback)
                .into(view);
    }

    @BindingAdapter(value = {"listItem", "onClick"})
    public static void bindMovieListItems(@NonNull RecyclerView listView,
                                          @Nullable List<Movie> items,
                                          @NonNull MovieAdapter.OnMovieDetailListener listener) {
        MovieAdapter adapter = (MovieAdapter) listView.getAdapter();
        if (adapter == null) {
            adapter = new MovieAdapter();
            // layout Manager
            listView.setLayoutManager(new LinearLayoutManager(
                    listView.getContext(), LinearLayoutManager.VERTICAL, false));
            // 구분선 세팅
            final int outSideSpace = 0;
            final int divHeight = listView.getContext()
                    .getResources()
                    .getDimensionPixelSize(R.dimen.divider_large_16dp);
            listView.addItemDecoration(new VerticalRecyclerViewDecoration(outSideSpace, divHeight));
            // listener
            adapter.setOnMovieDetailListener(listener);
            listView.setAdapter(adapter);
        }
        // item 세팅
        if (items != null) {
            adapter.setItems(items);
        }
    }

    @BindingAdapter(value = {"status"})
    public static void bindStatus(@NonNull ImageView view,
                                          @Nullable String status) {
        if(status == null) {
            return;
        }

        int image;
        switch (status) {
            case "Released":
                image = R.drawable.ic_released;
                break;
            default:
                image = R.drawable.ic_production;
                break;
        }
        view.setImageResource(image);
    }

    @BindingAdapter(value = {"listItem", "listener"})
    public static void bindPosterThumbnailList(@NonNull RecyclerView listView,
                                               @Nullable List<Image> items,
                                               @NonNull PosterThumbnailAdapter.OnClickListener listener) {
        PosterThumbnailAdapter adapter = (PosterThumbnailAdapter) listView.getAdapter();
        if (adapter == null) {
            adapter = new PosterThumbnailAdapter();

            // layout Manager
            listView.setLayoutManager(new LinearLayoutManager(
                    listView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            // 구분선 세팅
            final int outSideSpace = listView.getContext()
                    .getResources()
                    .getDimensionPixelSize(R.dimen.divider_large_16dp);
            final int divWidth = listView.getContext()
                    .getResources()
                    .getDimensionPixelSize(R.dimen.divider_small_4dp);
            listView.addItemDecoration(new HorizontalRecyclerViewDecoration(outSideSpace, divWidth));
            // listener
            adapter.setOnClickListener(listener);
            listView.setAdapter(adapter);
        }
        // items 세팅
        if (items != null) {
            adapter.setItems(items);
        }
    }

    @BindingAdapter(value = {"listItem", "listener"})
    public static void bindVideoList(@NonNull RecyclerView listView,
                                     @Nullable List<Video> items,
                                     @NonNull VideoAdapter.OnClickListener listener) {
        VideoAdapter adapter = (VideoAdapter) listView.getAdapter();
        if (adapter == null) {
            adapter = new VideoAdapter();

            // layout Manager
            listView.setLayoutManager(new LinearLayoutManager(
                    listView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            // 구분선 세팅
            final int outSideSpace = listView.getContext()
                    .getResources()
                    .getDimensionPixelSize(R.dimen.divider_large_16dp);
            final int divWidth = listView.getContext()
                    .getResources()
                    .getDimensionPixelSize(R.dimen.divider_small_4dp);
            listView.addItemDecoration(new HorizontalRecyclerViewDecoration(outSideSpace, divWidth));
            // listener
            adapter.setOnClickListener(listener);
            listView.setAdapter(adapter);
        }
        // items 세팅
        if (items != null) {
            adapter.setItems(items);
        }
    }

    @BindingAdapter(value = {"listItem", "currentPos"})
    public static void bindPosterList(@NonNull ViewPager2 viewpager,
                                      @Nullable List<Image> items,
                                      @Nullable Integer currentPos) {
        PosterAdapter adapter = (PosterAdapter) viewpager.getAdapter();
        if (adapter == null) {
            adapter = new PosterAdapter();
        }
        // item 세팅
        if (items != null) {
            adapter.setItems(items);
            Timber.d("PosterAdapter Items : %s", items.size());
        }
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(5);
        if (currentPos != null) {
            viewpager.setCurrentItem(currentPos, false);
            Timber.d("PosterAdapter Current Pos : %s", currentPos);
        }
    }
}
