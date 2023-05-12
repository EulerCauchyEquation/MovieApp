package com.hwonchul.movie.presentation.auth

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.hwonchul.movie.R
import com.hwonchul.movie.databinding.FragmentPhoneAuthCheckBinding
import com.hwonchul.movie.presentation.login.LoginState
import com.hwonchul.movie.presentation.login.LoginViewModel
import com.hwonchul.movie.util.StringUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhoneAuthCheckFragment : Fragment(R.layout.fragment_phone_auth_check) {
    private var _binding: FragmentPhoneAuthCheckBinding? = null
    private lateinit var navController: NavController
    private val viewModel: LoginViewModel by hiltNavGraphViewModels(R.id.login_graph)
    private lateinit var progressDialog: AlertDialog

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhoneAuthCheckBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = findNavController()

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        setProgressDialog()

        setRequestSmsCodeAgainClickListener()
        setSmsCodeClearClickListener()
        setEditSmsCodeAddTextChangedListener()
        setVerifyClickListener()

        observeVerificationResult()
        observeLoginResult()
        observeSmsCodeFormState()
        observeTimeRemaining()
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

    private fun observeVerificationResult() {
        viewModel.verificationResult.observe(viewLifecycleOwner) { result ->
            if (result !is PhoneAuthState.Loading) {
                // 로딩 아닐 때는 progress bar 해제
                progressDialog.dismiss()
            }

            when (result) {
                is PhoneAuthState.VerificationCompleted -> {
                    hideKeyboard()
                    binding.editSmsCode.setText(result.smsCode)
                }

                is PhoneAuthState.VerificationFailed -> showSnackBarMessage(getString(result.message))
                is PhoneAuthState.Loading -> progressDialog.show()
                else -> {}
            }
        }
    }

    private fun observeLoginResult() {
        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            if (result !is LoginState.Loading) {
                // 로딩 아닐 때는 progress bar 해제
                progressDialog.dismiss()
            }

            when (result) {
                is LoginState.LoginSuccess -> navController.navigate(
                    PhoneAuthCheckFragmentDirections.navigateToMain()
                )

                is LoginState.SignUp -> navController.navigate(
                    PhoneAuthCheckFragmentDirections.navigateToNick()
                )

                is LoginState.Failure -> showSnackBarMessage(getString(result.message))
                is LoginState.Loading -> progressDialog.show()
            }
        }
    }

    private fun observeSmsCodeFormState() {
        viewModel.smsCodeFormState.observe(viewLifecycleOwner) { smsCodeFormState ->
            if (smsCodeFormState.isDataValid) {
                binding.btnVerify.isEnabled = true
                binding.btnVerify.setBackgroundResource(R.drawable.bg_rect_blue_rounded_8dp)
            } else {
                binding.btnVerify.isEnabled = false
                binding.btnVerify.setBackgroundResource(R.drawable.bg_rect_gray_rounded_8dp)
            }
        }
    }

    private fun observeTimeRemaining() {
        viewModel.timeRemaining.observe(viewLifecycleOwner) { time ->
            binding.tvTimer.text = StringUtil.formatCountTime(time)
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.editSmsCode.windowToken, 0)
    }

    private fun showSnackBarMessage(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

