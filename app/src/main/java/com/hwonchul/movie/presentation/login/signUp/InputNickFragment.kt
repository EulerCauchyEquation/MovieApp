package com.hwonchul.movie.presentation.login.signUp

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
import com.hwonchul.movie.databinding.FragmentInputNickBinding
import com.hwonchul.movie.presentation.login.LoginContract.LoginState
import com.hwonchul.movie.presentation.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InputNickFragment : BaseFragment<FragmentInputNickBinding>(R.layout.fragment_input_nick) {
    private val viewModel: LoginViewModel by hiltNavGraphViewModels(R.id.login_graph)
    private lateinit var progressDialog: AlertDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    override fun setObserve() {
        observeState()
        observeNickNameFormState()
    }

    override fun setupView() {
        setProgressDialog()

        setCompleteClickListener()
        setSmsCodeClearClickListener()
        setEditNickNameAddTextChangedListener()
    }

    private fun setProgressDialog() {
        progressDialog = AlertDialog.Builder(requireContext(), R.style.ProgressDialog)
            .apply {
                setView(layoutInflater.inflate(R.layout.dialog_progress, null))
                setCancelable(false)
            }.create()
    }

    private fun setCompleteClickListener() {
        binding.btnComplete.setOnClickListener {
            hideKeyboard()
            viewModel.registerUser(binding.editNick.text.toString())
        }
    }

    private fun setSmsCodeClearClickListener() {
        binding.btnEditClear.setOnClickListener { binding.editNick.setText("") }
    }

    private fun setEditNickNameAddTextChangedListener() {
        binding.editNick.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.nickNameChanged(s.toString())
                binding.btnEditClear.visibility =
                    if (s.toString().isNotEmpty()) View.VISIBLE else View.GONE
            }
        })
    }

    private fun observeState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            if (state !is LoginState.Loading) {
                // 로딩 아닐 때는 progress bar 해제
                progressDialog.dismiss()
            }

            when (state) {
                is LoginState.LoginSuccess -> navController.navigate(InputNickFragmentDirections.navigateToMain())
                is LoginState.Error -> showSnackBarMessage(getString(state.message))
                is LoginState.Loading -> progressDialog.show()
                else -> {}
            }
        }
    }

    private fun observeNickNameFormState() {
        viewModel.uiData.observe(viewLifecycleOwner) { data ->
            when (data.nickNameFormState.isDataValid) {
                true -> {
                    binding.btnComplete.isEnabled = true
                    binding.btnComplete.setBackgroundResource(R.drawable.bg_rect_blue_rounded_8dp)
                }

                false -> {
                    binding.btnComplete.isEnabled = false
                    binding.btnComplete.setBackgroundResource(R.drawable.bg_rect_gray_rounded_8dp)
                }
            }
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.editNick.windowToken, 0)
    }
}