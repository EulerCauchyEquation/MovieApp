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
import com.hwonchul.movie.databinding.FragmentPhoneAuthBinding
import com.hwonchul.movie.presentation.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhoneAuthFragment : Fragment(R.layout.fragment_phone_auth) {
    private var _binding: FragmentPhoneAuthBinding? = null
    private lateinit var navController: NavController
    private val viewModel: LoginViewModel by hiltNavGraphViewModels(R.id.login_graph)
    private lateinit var progressDialog: AlertDialog

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhoneAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = findNavController()

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        setProgressDialog()

        setEditPhoneAddTextChangedListener()
        setPhoneNumberClearClickListener()
        setRequestClickListener()

        observePhoneNumberFormState()
        observeVerificationResult()
    }

    private fun setProgressDialog() {
        progressDialog = AlertDialog.Builder(requireContext(), R.style.ProgressDialog)
            .apply {
                setView(layoutInflater.inflate(R.layout.dialog_progress, null))
                setCancelable(false)
            }.create()
    }

    private fun setEditPhoneAddTextChangedListener() {
        binding.editPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.phoneNumberChanged(s.toString())
                binding.btnPhoneNumClear.visibility =
                    if (s.toString().isNotEmpty()) View.VISIBLE else View.GONE
            }
        })
    }

    private fun setPhoneNumberClearClickListener() {
        binding.btnPhoneNumClear.setOnClickListener {
            binding.editPhone.setText("")
        }
    }

    private fun setRequestClickListener() {
        binding.btnRequest.setOnClickListener {
            binding.editPhone.clearFocus()
            hideKeyboard()
            viewModel.requestSmsCodeToPhoneNumber(
                binding.editPhone.text.toString(),
                requireActivity()
            )
        }
    }

    private fun observePhoneNumberFormState() {
        viewModel.phoneNumberFormState.observe(viewLifecycleOwner) { phoneFormState ->
            if (phoneFormState.isDataValid) {
                binding.btnRequest.isEnabled = true
                binding.btnRequest.setBackgroundResource(R.drawable.bg_rect_blue_rounded_8dp)
            } else {
                binding.btnRequest.isEnabled = false
                binding.btnRequest.setBackgroundResource(R.drawable.bg_rect_gray_rounded_8dp)
            }
        }
    }

    private fun observeVerificationResult() {
        viewModel.verificationResult.observe(viewLifecycleOwner) { result ->
            if (result !is PhoneAuthState.Loading) {
                // 로딩 아닐 때는 progress bar 해제
                progressDialog.dismiss()
            }

            when (result) {
                is PhoneAuthState.VerificationFailed -> showSnackBarMessage(getString(result.message))
                is PhoneAuthState.Loading -> progressDialog.show()
                is PhoneAuthState.SmsCodeSent -> navController.navigate(PhoneAuthFragmentDirections.navigateToPhoneAuthCheck())
                else -> {}
            }
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.editPhone.windowToken, 0)
    }

    private fun showSnackBarMessage(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}