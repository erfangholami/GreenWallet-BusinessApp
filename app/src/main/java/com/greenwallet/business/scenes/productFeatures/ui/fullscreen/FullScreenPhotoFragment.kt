package com.greenwallet.business.scenes.productFeatures.ui.fullscreen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.greenwallet.business.databinding.FragmentFullScreenPhotoBinding
import com.greenwallet.business.network.product.response.FileMode
import com.greenwallet.business.scenes.productFeatures.ui.details.adapter.ImageSliderAdapter

class FullScreenPhotoFragment : Fragment(),
    FullScreenPhotoView {

    private lateinit var presenter: FullScreenPhotoView.Presenter

    private lateinit var binding: FragmentFullScreenPhotoBinding

    private var currentContext: Context? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFullScreenPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    override fun onStart() {
        super.onStart()
        presenter.subscribeView(this)
    }

    override fun onStop() {
        presenter.disposeView(this)
        super.onStop()
    }

    private fun setup() {

        binding.ivDismiss.setOnClickListener { presenter.onDismissFullScreenPhotoClicked() }

        binding.svImages.swipeDownListener = {
            presenter.onDismissFullScreenPhotoClicked()
        }

        val files = presenter.getProductFiles()
        val fileList = files.second

        binding.svImages.setOffscreenPageLimit(fileList.size)
        binding.svImages.setSliderAdapter(ImageSliderAdapter().apply {
            hasZoom = true
            when (files.first) {
                FileMode.URL -> {
                    mode = FileMode.URL
                }
                FileMode.ID -> {
                    mode = FileMode.ID
                }
            }
            items = fileList
            imageListener = { id, listener ->
                presenter.fetchImage(id, listener, null, false)
            }
        }, fileList.size > 1)

        binding.svImages.setCurrentPageListener {
            presenter.setCurrentPicturePosition(it)
        }

        binding.svImages.currentPagePosition = presenter.getShowingImagePosition()
        binding.svImages.pagerIndicator.selection = presenter.getShowingImagePosition()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is FullScreenPhotoPresenterProvider) {
            this.presenter = context.getFullScreenPhotoProvider()
        } else {
            throw RuntimeException("$context must implement FullScreenPhotoPresenterProvider")
        }

        currentContext = context
    }

    interface FullScreenPhotoPresenterProvider {
        fun getFullScreenPhotoProvider(): FullScreenPhotoView.Presenter
    }
}