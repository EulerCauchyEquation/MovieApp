package com.hwonchul.movie.presentation.auth

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.hwonchul.movie.R
import com.hwonchul.movie.base.view.BaseFragment
import com.hwonchul.movie.databinding.FragmentPhoneAuthCheckBinding
import com.hwonchul.movie.presentation.login.LoginContract.LoginState
import com.hwonchul.movie.presentation.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhoneAuthCheckFragment :
    BaseFragment<FragmentPhoneAuthCheckBinding>(R.layout.fragment_phone_auth_check) {
    private val viewModel: LoginViewModel by hiltNavGraphViewModels(R.id.login_graph)
    private lateinit var progressDialog: AlertDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun setObserve() {
        observeState()
        observeSmsCodeFormState()
    }

    override fun setupView() {
        setProgressDialog()

        setRequestSmsCodeAgainClickListener()
        setSmsCodeClearClickListener()
        setEditSmsCodeAddTextChangedListener()
        setVerifyClickListener()
    }

    private fun setProgressDialog() {
        progressDialog = AlertDialog.Builder(requireContext(), R.style.ProgressDialog)
            .apply {
                setView(layoutInflater.inflate(R.layout.dialog_progress, null))
                setCancelable(false)
            }.create()
    }

    private fun setRequestSmsCodeAgainClickListener() {
        binding.btnReRequest.setOnClickListener {
            viewModel.reRequestSmsCode(requireActivity())
        }
    }

    private fun setSmsCodeClearClickListener() {
        binding.btnSmsCodeClear.setOnClickListener {
            binding.editSmsCode.setText("")
        }
    }

    private fun setEditSmsCodeAddTextChangedListener() {
        binding.editSmsCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.smsCodeChanged(s.toString())
                binding.btnSmsCodeClear.visibility =
                    if (s.toString().isNotEmpty()) View.VISIBLE else View.GONE
            }
        })
    }

    private fun setVerifyClickListener() {
        binding.btnVerify.setOnClickListener {
            binding.editSmsCode.clearFocus()
            viewModel.verifyPhoneWithSmsCode(binding.editSmsCode.text.toString())
            hideKeyboard()
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
                is LoginState.PhoneAuth -> {
                    when (state.phoneAuthState) {
                        is PhoneAuthState.VerificationCompleted -> {
                            hideKeyboard()
                            binding.editSmsCode.setText(state.phoneAuthState.smsCode)
                        }

                        else -> {}
                    }
                }

                is LoginState.LoginSuccess -> navController.navigate(
                    PhoneAuthCheckFragmentDirections.navigateToMain()
                )

                is LoginState.SignUp -> navController.navigate(
                    PhoneAuthCheckFragmentDirections.navigateToNick()
                )

                is LoginState.Error -> showSnackBarMessage(getString(state.message))
                else -> {}
            }
        }
    }

    private fun observeSmsCodeFormState() {
        viewModel.uiData.observe(viewLifecycleOwner) { data ->
            if (data.smsCodeFormState.isDataValid) {
                binding.btnVerify.isEnabled = true
                binding.btnVerify.setBackgroundResource(R.drawable.bg_rect_blue_rounded_8dp)
            } else {
                binding.btnVerify.isEnabled = false
                binding.btnVerify.setBackgroundResource(R.drawable.bg_rect_gray_rounded_8dp)
            }
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.editSmsCode.windowToken, 0)
    }
}