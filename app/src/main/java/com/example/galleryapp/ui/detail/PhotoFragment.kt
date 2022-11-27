package com.example.galleryapp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.galleryapp.databinding.FragmentPhotoBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PhotoFragment : Fragment() {

    companion object {
        const val PHOTO_URL_KEY = "photo_url_key"
        const val FOLDER_NAME_KEY = "folder_name_key"
    }

    // Lazy inject ViewModel
    private val viewModel by viewModel<PhotoViewModel>()

    private var _binding: FragmentPhotoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val photoUrl
        get() = arguments?.getString(PHOTO_URL_KEY).orEmpty()

    private val folderName
        get() = arguments?.getString(FOLDER_NAME_KEY).orEmpty()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupVM()
    }

    private fun setupUI() {
        Glide.with(requireContext()).load(photoUrl).into(binding.ivPhoto)
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.cbAddFavorite.setOnClickListener {
            viewModel.addFavorite(photoUrl, folderName, binding.cbAddFavorite.isChecked)
        }
        viewModel.photoFavoriteObs.observe(viewLifecycleOwner) {
            binding.cbAddFavorite.isChecked = it
        }
    }

    private fun setupVM() {
        viewModel.isFavorite(photoUrl, folderName)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}