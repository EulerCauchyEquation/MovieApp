package com.hwonchul.movie.presentation.account

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.hwonchul.movie.R
import com.hwonchul.movie.base.view.BaseFragment
import com.hwonchul.movie.databinding.DialogConfirmBinding
import com.hwonchul.movie.databinding.FragmentAccountBinding
import com.hwonchul.movie.presentation.account.AccountContract.AccountState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : BaseFragment<FragmentAccountBinding>(R.layout.fragment_account) {
    private val viewModel: AccountViewModel by hiltNavGraphViewModels(R.id.account_graph)
    private lateinit var progressDialog: AlertDialog
    private lateinit var confirmDialog: AlertDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun setObserve() {
        observeUiState()
    }

    override fun setupView() {
        setProgressDialog()
        setWithdrawalConfirmDialog()

        setLogoutClickListener()
        setUserWithdrawalClickListener()
        setProfileEditClickListener()
    }

    private fun setProgressDialog() {
        progressDialog = AlertDialog.Builder(requireContext(), R.style.ProgressDialog)
            .apply {
                setView(layoutInflater.inflate(R.layout.dialog_progress, null))
                setCancelable(false)
            }.create()
    }

    private fun setLogoutClickListener() {
        binding.btnLogout.setOnClickListener { viewModel.logOut() }
    }

    private fun setUserWithdrawalClickListener() {
        binding.btnUserWithdraw.setOnClickListener { confirmDialog.show() }
    }

    private fun setWithdrawalConfirmDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_confirm, null)
        confirmDialog = AlertDialog.Builder(requireContext(), R.style.ConfirmDialog)
            .apply { setView(dialogView) }
            .create()

        val binding = DialogConfirmBinding.bind(dialogView)

        binding.tvMainTitle.text = getString(R.string.dialog_withdrawal_main_title)
        binding.tvSubTitle.text = getString(R.string.dialog_withdrawal_sub_title)

        binding.btnNegative.setOnClickListener { confirmDialog.dismiss() }
        binding.btnPositive.setOnClickListener {
            confirmDialog.dismiss()
            viewModel.withdrawal()
        }
    }

    private fun setProfileEditClickListener() {
        binding.btnProfileEdit.setOnClickListener {
            val user = viewModel.uiData.value!!.user
            navController.navigate(AccountFragmentDirections.navigateToProfileEdit(user))
        }
    }

    private fun observeUiState() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            if (uiState !is AccountState.Loading) {
                progressDialog.dismiss()
            }

            when (uiState) {
                is AccountState.Loading -> {
                    progressDialog.show()
                }

                is AccountState.LogoutSuccess -> {
                    showSnackBarMessage(getString(R.string.user_logout_success))
                    navController.navigate(AccountFragmentDirections.navigateToLoginGraph())
                }

                is AccountState.WithdrawalSuccess -> {
                    showSnackBarMessage(getString(R.string.user_withdrawal_success))
                    navController.navigate(AccountFragmentDirections.navigateToLoginGraph())
                }

                is AccountState.Idle -> {}
                is AccountState.Error -> showSnackBarMessage(getString(uiState.message))
            }
        }
    }
}