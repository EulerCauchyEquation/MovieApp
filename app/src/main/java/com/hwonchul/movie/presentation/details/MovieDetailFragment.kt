package com.hwonchul.movie.presentation.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.hwonchul.movie.R
import com.hwonchul.movie.base.view.BaseFragment
import com.hwonchul.movie.databinding.FragmentMovieDetailBinding
import com.hwonchul.movie.presentation.MainActivity
import com.hwonchul.movie.presentation.details.MovieDetailContract.MovieDetailState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment :
    BaseFragment<FragmentMovieDetailBinding>(R.layout.fragment_movie_detail) {
    private val viewModel: MovieDetailViewModel by hiltNavGraphViewModels(R.id.detail_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.tbMovieDetail.setupWithNavController(navController, appBarConfiguration)

        binding.fragment = this
        binding.viewModel = viewModel
        binding.videoListener = object : VideoAdapter.OnClickListener {
            override fun onClick(url: String) {
                val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse(url))
                startActivity(intent)
            }
        }
        binding.posterThumbnailListener = object : PosterThumbnailAdapter.OnClickListener {
            override fun onClick(currentPos: Int) {
                val navDirections = MovieDetailFragmentDirections.navigateToPoster(currentPos)
                navController.navigate(navDirections)
            }
        }
        // LiveData Lifecycleower 명시
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun setObserve() {
        observeState()
    }

    private fun observeState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is MovieDetailState.Idle -> showMainUI()
                is MovieDetailState.Error -> showError(getString(state.message))
                is MovieDetailState.Loading -> showLoading()
            }
        }
    }

    fun overviewTextMore() {
        binding.tvOverview.ellipsize = null
        binding.tvOverview.maxLines = Int.MAX_VALUE
        binding.btnOverviewTextMore.visibility = View.GONE
    }

    private fun showMainUI() {
        binding.progress.visibility = View.GONE
        binding.layoutLoading.visibility = View.GONE
        binding.appBarLayout.visibility = View.VISIBLE
        binding.layoutMain.visibility = View.VISIBLE

        // 자세히보기 세팅
        val synopsisLines = binding.tvOverview.lineCount
        if (synopsisLines == binding.tvOverview.maxLines) {
            binding.btnOverviewTextMore.visibility = View.VISIBLE
        } else {
            binding.btnOverviewTextMore.visibility = View.GONE
        }
    }

    private fun showError(message: String) {
        showSnackBarMessage(message)
        binding.progress.visibility = View.GONE
        binding.layoutLoading.visibility = View.GONE
        binding.appBarLayout.visibility = View.VISIBLE
        binding.layoutMain.visibility = View.VISIBLE
    }

    private fun showLoading() {
        binding.progress.visibility = View.VISIBLE
        binding.layoutLoading.visibility = View.VISIBLE
        binding.appBarLayout.visibility = View.GONE
        binding.layoutMain.visibility = View.GONE
    }
}