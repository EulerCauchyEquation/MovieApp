package com.hwonchul.movie.presentation.home.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.hwonchul.movie.R
import com.hwonchul.movie.databinding.FragmentMovieListBinding
import com.hwonchul.movie.domain.model.Movie
import com.hwonchul.movie.domain.model.MovieListType
import com.hwonchul.movie.presentation.home.HomeContract
import com.hwonchul.movie.presentation.home.HomeViewModel

class MovieListFragment : Fragment() {
    private var _binding: FragmentMovieListBinding? = null
    private lateinit var navController: NavController
    private val viewModel: HomeViewModel by hiltNavGraphViewModels(R.id.home_graph)
    private val args: MovieListFragmentArgs by navArgs()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = NavHostFragment.findNavController(this)

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.tbList.setupWithNavController(navController, appBarConfiguration)

        binding.listener = object : MovieDetailAdapter.OnClickListener {
            override fun onClick(movie: Movie) {
                val directions = MovieListFragmentDirections.navigateToDetailGraph(movie)
                navController.navigate(directions)
            }
        }
        binding.lifecycleOwner = viewLifecycleOwner

        observeState()

        setTabLayout()
        setOnRefreshListener()
    }

    private fun observeState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            if (state !is HomeContract.HomeState.Loading) {
                // 로딩 아닐 때는 progress bar 해제
                binding.progress.visibility = View.GONE
                binding.swipeLayout.isRefreshing = false
            }

            when (state) {
                is HomeContract.HomeState.Idle -> {}
                is HomeContract.HomeState.Error -> {
                    val error = getString(state.message)
                    showSnackbar(error)
                }

                is HomeContract.HomeState.Loading -> {
                    binding.progress.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setTabLayout() {
        val tabLayout = binding.tabLayout
        MovieListTabItem.values().forEach {
            tabLayout.addTab(tabLayout.newTab().setText(it.titleResId).setTag(it))
        }

        // 초기 세팅
        when (args.movieListType) {
            MovieListType.NowPlaying -> {
                binding.data = viewModel.uiData.value?.popularMovieList
                tabLayout.selectTab(tabLayout.getTabAt(MovieListTabItem.NOW_PLAYING.ordinal))
            }

            MovieListType.UpComing -> {
                binding.data = viewModel.uiData.value?.upComingMovieList
                tabLayout.selectTab(tabLayout.getTabAt(MovieListTabItem.UPCOMING.ordinal))
            }
        }
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.tag) {
                    MovieListTabItem.NOW_PLAYING -> {
                        binding.data = viewModel.uiData.value?.popularMovieList
                    }

                    MovieListTabItem.UPCOMING -> {
                        binding.data = viewModel.uiData.value?.upComingMovieList
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // 탭이 선택에서 해제될 때
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // 선택된 탭을 다시 선택 시
            }
        })
    }

    private fun setOnRefreshListener() {
        binding.swipeLayout.setOnRefreshListener { viewModel.refresh() }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}