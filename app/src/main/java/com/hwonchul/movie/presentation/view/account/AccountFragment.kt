package com.hwonchul.movie.presentation.view.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.hwonchul.movie.R
import com.hwonchul.movie.databinding.DialogConfirmBinding
import com.hwonchul.movie.databinding.FragmentAccountBinding
import com.hwonchul.movie.presentation.viewModel.account.AccountState
import com.hwonchul.movie.presentation.viewModel.account.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : Fragment(R.layout.fragment_account) {
    private var _binding: FragmentAccountBinding? = null
    private lateinit var navController: NavController
    private val viewModel: AccountViewModel by hiltNavGraphViewModels(R.id.account_graph)
    private lateinit var progressDialog: AlertDialog
    private lateinit var confirmDialog: AlertDialog

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = findNavController()

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        setProgressDialog()

        setLogoutClickListener()
        setUserWithdrawalClickListener()
        setWithdrawalConfirmDialog()
        setProfileEditClickListener()

        observeUserInfo()
        observeLogoutResult()
        observeWithdrawalResult()
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
            navController.navigate(AccountFragmentDirections.navigateToProfileEdit())
        }
    }

    private fun observeUserInfo() {
        viewModel.userInfo.observe(viewLifecycleOwner) { state ->
            if (state !is AccountState.Loading) {
                progressDialog.dismiss()
                binding.layout.visibility = View.VISIBLE
            }

            when (state) {
                is AccountState.Loading -> {
                    progressDialog.show()
                    binding.layout.visibility = View.INVISIBLE
                }
                is AccountState.Success -> {
                    binding.tvUserPhone.text = state.user?.phone
                    binding.tvUserNick.text = state.user?.nickname
                }
                is AccountState.Failure -> showSnackBarMessage(getString(state.message))
            }
        }
    }

    private fun observeLogoutResult() {
        viewModel.logoutResult.observe(viewLifecycleOwner) { state ->
            if (state !is AccountState.Loading) {
                progressDialog.dismiss()
            }

            when (state) {
                is AccountState.Loading -> progressDialog.show()
                is AccountState.Success -> {
                    showSnackBarMessage(getString(R.string.user_logout_success))
                    navController.navigate(AccountFragmentDirections.navigateToLoginGraph())
                }
                is AccountState.Failure -> showSnackBarMessage(getString(state.message))
            }
        }
    }

    private fun observeWithdrawalResult() {
        viewModel.withdrawalResult.observe(viewLifecycleOwner) { state ->
            if (state !is AccountState.Loading) {
                progressDialog.dismiss()
            }

            when (state) {
                is AccountState.Loading -> progressDialog.show()
                is AccountState.Success -> {
                    showSnackBarMessage(getString(R.string.user_withdrawal_success))
                    navController.navigate(AccountFragmentDirections.navigateToLoginGraph())
                }
                is AccountState.Failure -> showSnackBarMessage(getString(state.message))
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