package com.hwonchul.movie.presentation.details.poster

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.hwonchul.movie.R
import com.hwonchul.movie.databinding.FragmentPosterBinding
import com.hwonchul.movie.presentation.details.MovieDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PosterFragment : Fragment(R.layout.fragment_poster) {
    private var _binding: FragmentPosterBinding? = null
    private lateinit var callback: ViewPager2.OnPageChangeCallback
    private val viewModel: MovieDetailViewModel by hiltNavGraphViewModels(R.id.detail_graph)
    private val args: PosterFragmentArgs by navArgs()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPosterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = NavHostFragment.findNavController(this)

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.tbPoster.setupWithNavController(navController, appBarConfiguration)

        binding.viewModel = viewModel
        binding.currentPos = args.posterCurrentPos
        binding.lifecycleOwner = viewLifecycleOwner

        callback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tvPosterPageNo.text =
                    "${position + 1} / ${binding.vpPoster.adapter!!.itemCount}"
            }
        }
        binding.vpPoster.registerOnPageChangeCallback(callback)
        viewModel.uiData.observe(viewLifecycleOwner) { data ->
            binding.tvPosterPageNo.text =
                "${binding.vpPoster.currentItem + 1} / ${data.movieDetail.posters?.size}"
        }
    }

    override fun onDestroyView() {
        binding.vpPoster.unregisterOnPageChangeCallback(callback)
        super.onDestroyView()
    }
}