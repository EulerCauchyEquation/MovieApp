package com.hwonchul.movie.presentation.listing

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar.OnMenuItemClickListener
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.hwonchul.movie.R
import com.hwonchul.movie.databinding.FragmentMovieListBinding
import com.hwonchul.movie.domain.model.Movie
import com.hwonchul.movie.presentation.MainActivity
import com.hwonchul.movie.presentation.listing.MovieAdapter.OnMovieDetailListener
import com.hwonchul.movie.presentation.listing.MovieListContract.MovieListState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieListFragment : Fragment(), OnMenuItemClickListener {
    private var _binding: FragmentMovieListBinding? = null
    private lateinit var navController: NavController
    private val viewModel: MovieListViewModel by hiltNavGraphViewModels(R.id.list_graph)

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

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.movie_list))
        binding.tbMovieList.setupWithNavController(navController, appBarConfiguration)

        // menu 의 onOptionsItemSelected 담당
        // menu 의 onCreateOptionMenu 는 toolbar 위젯 내 app:menu 로 세팅
        binding.tbMovieList.setOnMenuItemClickListener(this)

        binding.viewModel = viewModel
        binding.listener = object : OnMovieDetailListener {
            override fun onClick(movie: Movie) {
                navigateToMovieDetail(movie)
            }
        }
        binding.lifecycleOwner = viewLifecycleOwner

        observeState()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.tb_profile -> {
                navController.navigate(MovieListFragmentDirections.navigateToAccount())
            }
        }
        return false
    }

    private fun observeState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            if (state !is MovieListState.Loading) {
                // 로딩 아닐 때는 progress bar 해제
                binding.progress.visibility = View.GONE
            }

            when (state) {
                is MovieListState.Idle -> {}
                is MovieListState.Error -> {
                    val error = getString(state.message)
                    showSnackbarMessage(error)
                }

                is MovieListState.Loading -> binding.progress.visibility = View.VISIBLE
            }
        }
    }

    private fun navigateToMovieDetail(movie: Movie) {
        val directions = MovieListFragmentDirections.navigateToDetailGraph(movie) as NavDirections
        navController.navigate(directions)
    }

    private fun showSnackbarMessage(message: String) {
        (activity as MainActivity).showSnackbar(message)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}