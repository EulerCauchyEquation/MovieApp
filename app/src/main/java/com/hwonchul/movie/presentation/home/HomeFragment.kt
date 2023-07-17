package com.hwonchul.movie.presentation.home

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar.OnMenuItemClickListener
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.NavDirections
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.hwonchul.movie.R
import com.hwonchul.movie.base.view.BaseFragment
import com.hwonchul.movie.databinding.FragmentHomeBinding
import com.hwonchul.movie.domain.model.Movie
import com.hwonchul.movie.domain.model.MovieListType
import com.hwonchul.movie.presentation.home.HomeContract.HomeState
import com.hwonchul.movie.presentation.home.MovieAdapter.OnMovieDetailListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home),
    OnMenuItemClickListener {
    private val viewModel: HomeViewModel by hiltNavGraphViewModels(R.id.home_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.home))
        binding.tbHome.setupWithNavController(navController, appBarConfiguration)

        // menu 의 onOptionsItemSelected 담당
        // menu 의 onCreateOptionMenu 는 toolbar 위젯 내 app:menu 로 세팅
        binding.tbHome.setOnMenuItemClickListener(this)

        binding.viewModel = viewModel
        binding.listener = object : OnMovieDetailListener {
            override fun onClick(movie: Movie) {
                navigateToMovieDetail(movie)
            }
        }
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun getSystemBar() = SystemBar(
        ContextCompat.getColor(requireActivity(), R.color.colorPrimary),
        ContextCompat.getColor(requireActivity(), R.color.colorPrimary),
    )

    override fun setObserve() {
        observeState()
    }

    override fun setupView() {
        setOnMorePopularListClickListener()
        setOnMoreUpcomingListClickListener()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_profile -> navController.navigate(HomeFragmentDirections.navigateToAccount())
            R.id.item_search -> navController.navigate(HomeFragmentDirections.navigateToSearch())
        }
        return false
    }

    private fun observeState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            if (state !is HomeState.Loading) {
                // 로딩 아닐 때는 progress bar 해제
                binding.progress.visibility = View.GONE
                binding.layout.visibility = View.VISIBLE
            }

            when (state) {
                is HomeState.Idle -> {}
                is HomeState.Error -> {
                    val error = getString(state.message)
                    showSnackBarMessage(error)
                }

                is HomeState.Loading -> {
                    binding.progress.visibility = View.VISIBLE
                    binding.layout.visibility = View.GONE
                }
            }
        }
    }

    private fun setOnMorePopularListClickListener() {
        binding.ivMorePopularList.setOnClickListener {
            val directions = HomeFragmentDirections.navigateToListMore(MovieListType.NowPlaying)
            navController.navigate(directions)
        }
    }

    private fun setOnMoreUpcomingListClickListener() {
        binding.ivMoreUpcomingList.setOnClickListener {
            val directions = HomeFragmentDirections.navigateToListMore(MovieListType.UpComing)
            navController.navigate(directions)
        }
    }

    private fun navigateToMovieDetail(movie: Movie) {
        val directions = HomeFragmentDirections.navigateToDetailGraph(movie) as NavDirections
        navController.navigate(directions)
    }
}