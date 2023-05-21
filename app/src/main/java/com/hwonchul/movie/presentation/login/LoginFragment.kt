package com.hwonchul.movie.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.hwonchul.movie.R
import com.hwonchul.movie.databinding.FragmentLoginBinding
import com.hwonchul.movie.presentation.login.LoginContract.LoginState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private var _binding: FragmentLoginBinding? = null
    private val viewModel: LoginViewModel by hiltNavGraphViewModels(R.id.login_graph)
    private lateinit var navController: NavController
    private lateinit var progressDialog: AlertDialog

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = NavHostFragment.findNavController(this)

        setProgressDialog()
        setStartClickListener()
        observeState()
    }

    private fun setProgressDialog() {
        progressDialog = AlertDialog.Builder(requireContext(), R.style.ProgressDialog)
            .apply {
                setView(layoutInflater.inflate(R.layout.dialog_progress, null))
                setCancelable(false)
            }.create()
    }

    private fun setStartClickListener() {
        binding.btnStart.setOnClickListener {
            navController.navigate(LoginFragmentDirections.navigateToPhoneAuth())
        }
    }

    private fun observeState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            if (state !is LoginState.Loading) {
                // 로딩 아닐 때는 progress bar 해제
                progressDialog.dismiss()
            }

            when (state) {
                is LoginState.Loading -> progressDialog.show()
                is LoginState.Error -> showSnackBarMessage(getString(state.message))
                else -> {}
            }
        }
    }

    private fun showSnackBarMessage(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}