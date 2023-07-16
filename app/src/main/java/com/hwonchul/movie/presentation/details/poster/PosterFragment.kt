package com.hwonchul.movie.presentation.details.poster

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.hwonchul.movie.R
import com.hwonchul.movie.base.view.BaseFragment
import com.hwonchul.movie.databinding.FragmentPosterBinding
import com.hwonchul.movie.presentation.details.MovieDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PosterFragment : BaseFragment<FragmentPosterBinding>(R.layout.fragment_poster) {
    private val viewModel: MovieDetailViewModel by hiltNavGraphViewModels(R.id.detail_graph)
    private val args: PosterFragmentArgs by navArgs()

    private lateinit var callback: ViewPager2.OnPageChangeCallback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.tbPoster.setupWithNavController(navController, appBarConfiguration)

        binding.viewModel = viewModel
        binding.currentPos = args.posterCurrentPos
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun getSystemBar() = SystemBar(
        ContextCompat.getColor(requireActivity(), android.R.color.black),
        ContextCompat.getColor(requireActivity(), android.R.color.black),
    )

    override fun setObserve() {
        viewModel.uiData.observe(viewLifecycleOwner) { data ->
            binding.tvPosterPageNo.text =
                "${binding.vpPoster.currentItem + 1} / ${data.movieDetail.posters?.size}"
        }
    }

    override fun setupView() {
        callback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tvPosterPageNo.text =
                    "${position + 1} / ${binding.vpPoster.adapter!!.itemCount}"
            }
        }
        binding.vpPoster.registerOnPageChangeCallback(callback)
    }

    override fun onDestroyView() {
        binding.vpPoster.unregisterOnPageChangeCallback(callback)
        super.onDestroyView()
    }
}