package com.greenwallet.business.scenes.findMySize.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.greenwallet.business.databinding.FragmentFindMySizeBinding
import com.greenwallet.business.scenes.productFeatures.ui.details.adapter.ImageSliderAdapter

class FindMySizeFragment : Fragment(), FindMySizeView {

    private lateinit var presenter: FindMySizeView.Presenter

    private lateinit var binding: FragmentFindMySizeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFindMySizeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBackButton.setOnClickListener { presenter.onCloseFindMySize() }
        showSizesInfo()
    }

    private fun showSizesInfo() {

        binding.svImages.setSliderAdapter(ImageSliderAdapter().apply {
            hasZoom = true
            items = presenter.getSizesFiles()
            imageListener = { id, listener ->
                presenter.fetchImage(id, listener, null,false)
            }
        }, false)
    }

    override fun onStart() {
        super.onStart()
        presenter.subscribeView(this)
    }

    override fun onStop() {
        presenter.disposeView(this)
        super.onStop()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is FindMySizeFeatureProvider) {
            this.presenter = context.getFindMySizeFeatureProvider()
        } else {
            throw RuntimeException("$context must implement FindMySizeFeatureProvider")
        }
    }

    interface FindMySizeFeatureProvider {
        fun getFindMySizeFeatureProvider(): FindMySizeView.Presenter
    }
}