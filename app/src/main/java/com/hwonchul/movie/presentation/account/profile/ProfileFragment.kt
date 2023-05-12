package com.hwonchul.movie.presentation.account.profile

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.hwonchul.movie.R
import com.hwonchul.movie.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private var _binding: FragmentProfileBinding? = null
    private lateinit var navController: NavController
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var progressDialog: AlertDialog

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = findNavController()

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        setProgressDialog()

        setEditNicknameAddTextChangedListener()
        setCompleteClickListener()

        observeNickNameFormState()
        observeProfileResult()
    }

    private fun setCompleteClickListener() {
        binding.btnComplete.setOnClickListener {
            hideKeyboard()
            viewModel.updateUser()
        }
    }

    private fun setProgressDialog() {
        progressDialog = AlertDialog.Builder(requireContext(), R.style.ProgressDialog)
            .apply {
                setView(layoutInflater.inflate(R.layout.dialog_progress, null))
                setCancelable(false)
            }.create()
    }

    private fun setEditNicknameAddTextChangedListener() {
        binding.editNickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.nickNameChanged(s.toString())
            }
        })
    }

    private fun observeNickNameFormState() {
        viewModel.nickNameFormState.observe(viewLifecycleOwner) { smsCodeFormState ->
            if (smsCodeFormState.isDataValid) {
                binding.btnComplete.isEnabled = true
                binding.btnComplete.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.BLACK
                    )
                )
                binding.editNicknameLayout.isErrorEnabled = false
                binding.editNicknameLayout.error = null
            } else {
                binding.btnComplete.isEnabled = false
                binding.btnComplete.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.BLANK_COLOR
                    )
                )
                binding.editNicknameLayout.error = getString(R.string.profile_edit_form_error)
                binding.editNicknameLayout.isErrorEnabled = true
            }
        }
    }

    private fun observeProfileResult() {
        viewModel.profileResult.observe(viewLifecycleOwner) { state ->
            if (state !is ProfileState.Loading) {
                // 로딩 아닐 때는 progress bar 해제
                progressDialog.dismiss()
            }

            when (state) {
                is ProfileState.Success -> {
                    navController.navigateUp()
                    showSnackBarMessage(getString(R.string.profile_update_success))
                }
                is ProfileState.Failure -> showSnackBarMessage(getString(state.message))
                is ProfileState.Loading -> progressDialog.show()
            }
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.editNickname.windowToken, 0)
    }

    private fun showSnackBarMessage(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}