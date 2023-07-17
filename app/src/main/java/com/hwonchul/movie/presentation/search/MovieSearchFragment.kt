package com.hwonchul.movie.presentation.search

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.hwonchul.movie.R
import com.hwonchul.movie.base.view.BaseFragment
import com.hwonchul.movie.databinding.FragmentMovieSearchBinding
import com.hwonchul.movie.domain.model.Movie
import com.hwonchul.movie.presentation.home.list.MovieDetailAdapter
import com.hwonchul.movie.util.GridRecyclerViewDecoration
import com.hwonchul.movie.util.NetworkStatusHelper
import kotlinx.coroutines.launch

class MovieSearchFragment :
    BaseFragment<FragmentMovieSearchBinding>(R.layout.fragment_movie_search) {
    private val viewModel: MovieSearchViewModel by hiltNavGraphViewModels(R.id.search_graph)
    private lateinit var adapter: MovieDetailAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.tbSearch.setupWithNavController(navController, appBarConfiguration)
    }

    override fun getSystemBar() = SystemBar(
        ContextCompat.getColor(requireActivity(), R.color.colorPrimary),
        ContextCompat.getColor(requireActivity(), R.color.colorPrimary),
    )

    override fun setObserve() {
        observeUiData()
        observeNetworkStatusHelper()
    }

    private fun observeUiData() {
        viewModel.uiData.observe(viewLifecycleOwner) { uiData ->
            lifecycleScope.launch {
                adapter.submitData(uiData.pagedMovieList)
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

    override fun setupView() {
        setMovieDetailAdapter()
        setSearchClearClickListener()
        setSearchTextChangedListener()
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
        val listener = object : MovieDetailAdapter.OnClickListener {
            override fun onClick(movie: Movie) {
                hideKeyboard()

                val directions = MovieSearchFragmentDirections.navigateToDetail(movie)
                navController.navigate(directions)
            }
        }
        adapter.setOnClickListener(listener)

        binding.rvMovie.adapter = adapter

        adapter.addLoadStateListener { loadStates ->
            if (loadStates.refresh is LoadState.Error) {
                binding.rvMovie.visibility = View.GONE
                binding.layoutError.layout.visibility = View.VISIBLE
            } else {
                binding.rvMovie.visibility = View.VISIBLE
                binding.layoutError.layout.visibility = View.GONE
            }

            // 다음 Page 가져오기 실패 시 처리
            if (loadStates.append is LoadState.Error) {
                showSnackBarMessage(getString(R.string.all_response_failed))
            }

            // Empty View 세팅
            if (loadStates.append.endOfPaginationReached) {
                // 마지막 페이지에 도달했는데 아이템이 없는 경우만 empty view 를 세팅
                binding.layoutEmpty.layout.visibility =
                    if (adapter.itemCount == 0) View.VISIBLE else View.GONE
            } else {
                binding.layoutEmpty.layout.visibility = View.GONE
            }
        }
    }

    private fun setSearchTextChangedListener() {
        binding.editSearch.addTextChangedListener {
            viewModel.search(it.toString())
            binding.btnEditClear.visibility =
                if (it.toString().isNotEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun setSearchClearClickListener() {
        binding.btnEditClear.setOnClickListener { binding.editSearch.setText("") }
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.editSearch.windowToken, 0)
    }
}