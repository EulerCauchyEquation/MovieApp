package com.hwonchul.movie.base.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment<B : ViewBinding>(@LayoutRes private val layoutId: Int) :
    Fragment(layoutId) {

    private var _binding: B? = null
    protected lateinit var navController: NavController

    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = findNavController()

        val systemBar = getSystemBar()
        setSystemBar(
            systemBar.statusBarColor,
            systemBar.navigationBarColor,
            systemBar.isLightStatusBar,
            systemBar.isLightNavigationBar,
        )

        setObserve()
        setupView()
    }

    protected data class SystemBar(
        val statusBarColor: Int,
        val navigationBarColor: Int,
        val isLightStatusBar: Boolean = false,
        val isLightNavigationBar: Boolean = false,
    )

    protected open fun getSystemBar() = SystemBar(
        ContextCompat.getColor(requireActivity(), android.R.color.white),
        ContextCompat.getColor(requireActivity(), android.R.color.white),
        isLightStatusBar = true,
        isLightNavigationBar = true,
    )

    protected open fun setObserve() {}

    protected open fun setupView() {}

    protected fun setSystemBar(
        statusBarColor: Int,
        navigationBarColor: Int,
        isLightStatusBar: Boolean = false,
        isLightNavigationBar: Boolean = false,
    ) {
        val mWindow = requireActivity().window

        mWindow.statusBarColor = statusBarColor
        mWindow.navigationBarColor = navigationBarColor

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            mWindow.insetsController?.apply {
                if (isLightStatusBar) {
                    setSystemBarsAppearance(
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    )
                } else {
                    setSystemBarsAppearance(
                        0,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    )
                }

                if (isLightNavigationBar) {
                    setSystemBarsAppearance(
                        WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                    )
                } else {
                    setSystemBarsAppearance(
                        0,
                        WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                    )
                }
            }
        } else @Suppress("DEPRECATION") {
            var uiVisibility = mWindow.decorView.systemUiVisibility
            uiVisibility = if (isLightStatusBar) {
                uiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                uiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            uiVisibility = if (isLightNavigationBar) {
                uiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            } else {
                uiVisibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
            }
            mWindow.decorView.systemUiVisibility = uiVisibility
        }
    }

    protected fun showSnackBarMessage(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}