package com.hwonchul.movie.presentation.login

import androidx.appcompat.app.AlertDialog
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.hwonchul.movie.R
import com.hwonchul.movie.base.view.BaseFragment
import com.hwonchul.movie.databinding.FragmentLoginBinding
import com.hwonchul.movie.presentation.login.LoginContract.LoginState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {
    private val viewModel: LoginViewModel by hiltNavGraphViewModels(R.id.login_graph)
    private lateinit var progressDialog: AlertDialog

    override fun setObserve() {
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

    override fun setupView() {
        setProgressDialog()
        setStartClickListener()
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
}