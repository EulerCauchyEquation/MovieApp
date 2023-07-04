package com.hwonchul.movie.presentation.account.profile

import android.content.Context
import android.net.Uri
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
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.hwonchul.movie.R
import com.hwonchul.movie.databinding.FragmentProfileBinding
import com.hwonchul.movie.presentation.account.profile.ProfileContract.ProfileState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private var _binding: FragmentProfileBinding? = null
    private lateinit var navController: NavController
    private val viewModel: ProfileViewModel by viewModels()
    private val args: ProfileFragmentArgs by navArgs()
    private lateinit var progressDialog: AlertDialog

    private lateinit var galleryImagePicker: GalleryImagePicker

    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is GalleryImagePicker) {
            galleryImagePicker = context
        }
    }

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
        binding.user = args.user
        binding.lifecycleOwner = viewLifecycleOwner

        setProgressDialog()

        setEditNicknameAddTextChangedListener()
        setCompleteClickListener()
        setUserImageClickListener()

        observeNickNameFormState()
        observeState()
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

    private fun setUserImageClickListener() {
        binding.ivUserImage.setOnClickListener {
            val items = resources.getStringArray(R.array.user_profile_image_options)

            AlertDialog.Builder(requireContext())
                .setItems(items) { _, which ->
                    when (items[which]) {
                        // 앨범에서 선택
                        getString(R.string.options_user_image_album) -> galleryImagePicker.openGallery()

                        // 프로필 사진 삭제
                        getString(R.string.options_user_image_delete) -> {
                            updateProfileImage(null)
                            binding.ivUserImage.setImageResource(R.drawable.ic_user_default)
                        }
                    }
                }
                .show()
        }
    }

    private fun observeNickNameFormState() {
        viewModel.uiData.observe(viewLifecycleOwner) { uiData ->
            when (uiData.nickNameFormState.isDataValid) {
                true -> {
                    binding.btnComplete.isEnabled = true
                    binding.btnComplete.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.BLACK
                        )
                    )
                    binding.editNicknameLayout.isErrorEnabled = false
                    binding.editNicknameLayout.error = null
                }

                false -> {
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
    }

    private fun observeState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            if (state !is ProfileState.Loading) {
                // 로딩 아닐 때는 progress bar 해제
                progressDialog.dismiss()
            }

            when (state) {
                is ProfileState.Loading -> progressDialog.show()
                is ProfileState.Idle -> {}
                is ProfileState.EditSuccess -> {
                    navController.navigateUp()
                    showSnackBarMessage(getString(R.string.profile_update_success))
                }

                is ProfileState.Error -> showSnackBarMessage(getString(state.message))
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

    fun updateProfileImage(imageUri: Uri?) {
        viewModel.userImageChanged(imageUri?.toString())
        binding.ivUserImage.setImageURI(imageUri)
    }

    interface GalleryImagePicker {
        fun openGallery()
    }
}