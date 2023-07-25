package com.hwonchul.movie.presentation.home.list

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.hwonchul.movie.R
import com.hwonchul.movie.base.view.BaseFragment
import com.hwonchul.movie.databinding.FragmentMovieListBinding
import com.hwonchul.movie.domain.model.Movie
import com.hwonchul.movie.domain.model.MovieListType
import com.hwonchul.movie.presentation.home.HomeContract
import com.hwonchul.movie.presentation.home.HomeViewModel
import com.hwonchul.movie.util.GridRecyclerViewDecoration
import com.hwonchul.movie.util.NetworkStatusHelper
import kotlinx.coroutines.launch

class MovieListFragment : BaseFragment<FragmentMovieListBinding>(R.layout.fragment_movie_list) {
    private val viewModel: HomeViewModel by hiltNavGraphViewModels(R.id.home_graph)
    private val args: MovieListFragmentArgs by navArgs()

    private lateinit var adapter: MovieDetailAdapter
    private lateinit var currentTab: MovieListTabItem

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.tbList.setupWithNavController(navController, appBarConfiguration)
    }

    override fun getSystemBar() = SystemBar(
        ContextCompat.getColor(requireActivity(), R.color.colorPrimary),
        ContextCompat.getColor(requireActivity(), R.color.colorPrimary),
    )

    override fun setObserve() {
        observeUIData()
        observeNetworkStatusHelper()
        observeState()
    }

    override fun setupView() {
        setMovieDetailAdapter()
        setTabLayout()
        setOnRefreshListener()
        setBtnRetryClickListener()
    }

    private fun observeUIData() {
        viewModel.uiData.observe(viewLifecycleOwner) { uiData ->
            lifecycleScope.launch {
                val movies = when (currentTab) {
                    MovieListTabItem.NOW_PLAYING -> uiData.pagedPopularMovieList
                    MovieListTabItem.UPCOMING -> uiData.pagedUpComingMovieList
                }
                adapter.submitData(movies)
            }
        }
    }

    private fun observeNetworkStatusHelper() {
        viewModel.networkStatusHelper.observe(viewLifecycleOwner) {
            if (it == NetworkStatusHelper.NetworkStatus.AVAILABLE) {
                adapter.retry()
            }
        }
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
                    showSnackBarMessage(error)
                }

                is HomeContract.HomeState.Loading -> {
                    binding.progress.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setMovieDetailAdapter() {
        adapter = MovieDetailAdapter()

        // 한 행의 열 개수
        val spanCount = 2

        // layout Manager
        binding.rvMovie.layoutManager = GridLayoutManager(context, spanCount)

        // 구분선 세팅
        val spacing = binding.rvMovie.context
            .resources
            .getDimensionPixelSize(R.dimen.divider_large_16dp)
        binding.rvMovie.addItemDecoration(GridRecyclerViewDecoration(spanCount, spacing))

        // listener
        val movieClickListener = object : MovieDetailAdapter.MovieClickListener {
            override fun onClick(movie: Movie) {
                val directions = MovieListFragmentDirections.navigateToDetailGraph(movie)
                navController.navigate(directions)
            }
        }
        adapter.setMovieClickListener(movieClickListener)

        val favoritesClickListener = object : MovieDetailAdapter.FavoritesClickListener {
            override fun onClick(movie: Movie, isFavorite: Boolean) {
                if (isFavorite) viewModel.removeFavorites(movie) else viewModel.addFavorites(movie)
            }
        }
        adapter.setFavoritesClickListener(favoritesClickListener)

        // LoadStateListener
        adapter.addLoadStateListener { loadStates ->
            binding.swipeLayout.isRefreshing =
                loadStates.append is LoadState.Loading && loadStates.refresh is LoadState.Loading

            // 최초 데이터 가져오기 실패 시 처리
            // Error 레이아웃을 표시
            if (loadStates.refresh is LoadState.Error) {
                binding.swipeLayout.visibility = View.GONE
                binding.layoutError.layout.visibility = View.VISIBLE
            } else {
                binding.swipeLayout.visibility = View.VISIBLE
                binding.layoutError.layout.visibility = View.GONE
            }

            // 다음 Page 가져오기 실패 시 처리
            if (loadStates.append is LoadState.Error) {
                showSnackBarMessage(getString(R.string.all_response_failed))
            }
        }

        binding.rvMovie.adapter = adapter
    }

    private fun setTabLayout() {
        // tab 세팅
        val tabLayout = binding.tabLayout
        MovieListTabItem.values().forEach {
            tabLayout.addTab(tabLayout.newTab().setText(it.titleResId).setTag(it))
        }

        // select 된 tab 의 Type 으로 영화 리스트 세팅
        val items = when (args.movieListType) {
            MovieListType.NowPlaying -> viewModel.uiData.value!!.pagedPopularMovieList
            MovieListType.UpComing -> viewModel.uiData.value!!.pagedUpComingMovieList
        }
        setMovieDetailAdapterWithData(items)
        currentTab = MovieListTabItem.getTabItemByType(args.movieListType)
        tabLayout.selectTab(tabLayout.getTabAt(currentTab.ordinal))

        // tab selectedListener 세팅
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                currentTab = tab.tag as MovieListTabItem
                when (currentTab.type) {
                    MovieListType.NowPlaying -> viewModel.uiData.value!!.pagedPopularMovieList
                    MovieListType.UpComing -> viewModel.uiData.value!!.pagedUpComingMovieList
                }.let { setMovieDetailAdapterWithData(it) }
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
        binding.swipeLayout.setOnRefreshListener { adapter.refresh() }
    }

    private fun setBtnRetryClickListener() {
        binding.layoutError.btnRetry.setOnClickListener { adapter.retry() }
    }

    private fun setMovieDetailAdapterWithData(items: PagingData<Movie>) {
        lifecycleScope.launch {
            adapter.submitData(items)
        }
    }
}