package com.hwonchul.movie.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.hwonchul.movie.R
import com.hwonchul.movie.domain.model.Image
import com.hwonchul.movie.domain.model.Movie
import com.hwonchul.movie.domain.model.Video
import com.hwonchul.movie.presentation.details.PosterThumbnailAdapter
import com.hwonchul.movie.presentation.details.VideoAdapter
import com.hwonchul.movie.presentation.details.poster.PosterAdapter
import com.hwonchul.movie.presentation.home.MovieAdapter
import com.hwonchul.movie.presentation.home.MovieAdapter.OnMovieDetailListener
import timber.log.Timber
import java.time.LocalDate

object BindingUtils {

    @JvmStatic
    @BindingAdapter(value = ["imageUrl", "placeholder", "fallback"], requireAll = false)
    fun bindImage(
        view: ImageView,
        url: String?,
        placeholder: Drawable?,
        fallback: Drawable?
    ) {
        Glide.with(view.context)
            .load(url)
            // key를 하루 단위로 변경한다.
            .signature(ObjectKey(LocalDate.now().toEpochDay()))
            .placeholder(placeholder)
            .fallback(fallback)
            .into(view)
    }

    @JvmStatic
    @BindingAdapter(value = ["listItem", "onClick"])
    fun bindMovieListItems(
        listView: RecyclerView,
        items: List<Movie>?,
        listener: OnMovieDetailListener
    ) {
        (listView.adapter as MovieAdapter? ?: MovieAdapter().also { adapter ->
            // layout Manager
            listView.layoutManager = LinearLayoutManager(
                listView.context, LinearLayoutManager.HORIZONTAL, false
            )

            // 구분선 세팅
            val outSideSpace = listView.context
                .resources
                .getDimensionPixelSize(R.dimen.divider_large_16dp)
            val divHeight = listView.context
                .resources
                .getDimensionPixelSize(R.dimen.divider_large_16dp)
            listView.addItemDecoration(HorizontalRecyclerViewDecoration(outSideSpace, divHeight))

            // listener
            adapter.setOnMovieDetailListener(listener)
            listView.adapter = adapter
        }).apply { items?.let { setItems(it) } }
    }

    @JvmStatic
    @BindingAdapter(value = ["status"])
    fun bindStatus(
        view: ImageView,
        status: String?
    ) {
        if (status == null) {
            return
        }
        val image = when (status) {
            "Released" -> R.drawable.ic_released
            else -> R.drawable.ic_production
        }
        view.setImageResource(image)
    }

    @JvmStatic
    @BindingAdapter(value = ["listItem", "listener"])
    fun bindPosterThumbnailList(
        listView: RecyclerView,
        items: List<Image>?,
        listener: PosterThumbnailAdapter.OnClickListener
    ) {
        (listView.adapter as PosterThumbnailAdapter? ?: PosterThumbnailAdapter().also { adapter ->
            // layout Manager
            listView.layoutManager = LinearLayoutManager(
                listView.context, LinearLayoutManager.HORIZONTAL, false
            )

            // 구분선 세팅
            val outSideSpace = listView.context
                .resources
                .getDimensionPixelSize(R.dimen.divider_large_16dp)
            val divWidth = listView.context
                .resources
                .getDimensionPixelSize(R.dimen.divider_small_4dp)
            listView.addItemDecoration(HorizontalRecyclerViewDecoration(outSideSpace, divWidth))

            // listener
            adapter.setOnClickListener(listener)
            listView.adapter = adapter
        }).apply { items?.let { setItems(it) } }
    }

    @JvmStatic
    @BindingAdapter(value = ["listItem", "listener"])
    fun bindVideoList(
        listView: RecyclerView,
        items: List<Video>?,
        listener: VideoAdapter.OnClickListener
    ) {
        (listView.adapter as VideoAdapter? ?: VideoAdapter().also { adapter ->
            // layout Manager
            listView.layoutManager = LinearLayoutManager(
                listView.context, LinearLayoutManager.HORIZONTAL, false
            )

            // 구분선 세팅
            val outSideSpace = listView.context
                .resources
                .getDimensionPixelSize(R.dimen.divider_large_16dp)
            val divWidth = listView.context
                .resources
                .getDimensionPixelSize(R.dimen.divider_small_4dp)
            listView.addItemDecoration(HorizontalRecyclerViewDecoration(outSideSpace, divWidth))

            // listener
            adapter.setOnClickListener(listener)
            listView.adapter = adapter
        }).apply { items?.let { setItems(it) } }
    }

    @JvmStatic
    @BindingAdapter(value = ["listItem", "currentPos"])
    fun bindPosterList(
        viewpager: ViewPager2,
        items: List<Image>?,
        currentPos: Int?
    ) {
        (viewpager.adapter as PosterAdapter? ?: PosterAdapter().also { adapter ->
            viewpager.adapter = adapter
            viewpager.offscreenPageLimit = 5
        }).apply {
            items?.let {
                setItems(it)
                Timber.d("PosterAdapter Items : %s", it.size)
            }
            currentPos?.let {
                viewpager.setCurrentItem(it, false)
                Timber.d("PosterAdapter Current Pos : %s", it)
            }
        }
    }
}