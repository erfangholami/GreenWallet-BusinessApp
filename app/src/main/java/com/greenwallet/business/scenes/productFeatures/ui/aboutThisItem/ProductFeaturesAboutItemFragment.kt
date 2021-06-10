package com.greenwallet.business.scenes.productFeatures.ui.aboutThisItem

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.greenwallet.business.databinding.FragmentProductDetailsAboutItemBinding

class ProductFeaturesAboutItemFragment : Fragment(),
    ProductFeaturesAboutItemView {

    private lateinit var presenter: ProductFeaturesAboutItemView.Presenter

    private lateinit var binding: FragmentProductDetailsAboutItemBinding

    private var currentContext: Context? = null

    private var data: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            data = requireArguments().getString(ARG_PARAM_DATA).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailsAboutItemBinding.inflate(inflater, container, false)
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
        binding.ivBackButton.setOnClickListener { presenter.onDismissAboutItemClicked() }

        binding.wvDescription.isScrollContainer = false
        binding.wvDescription.isScrollbarFadingEnabled = true
        binding.wvDescription.isVerticalScrollBarEnabled = true
        binding.wvDescription.isHorizontalScrollBarEnabled = false
        binding.wvDescription.scrollBarStyle = View.SCROLLBARS_OUTSIDE_OVERLAY
        binding.wvDescription.setBackgroundColor(Color.TRANSPARENT)

        var htmlToShow = data

        if (data.contains("font-weight: 1")) {
            htmlToShow = data.replace("font-weight: 1", "font-weight: 300")
        }

        binding.wvDescription.loadData(htmlToShow, "text/html", "utf-8")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ProductFeaturesAboutItemPresenterProvider) {
            this.presenter = context.getProductFeaturesAboutItemProvider()
        } else {
            throw RuntimeException("$context must implement ProductFeaturesAboutItemPresenterProvider")
        }

        currentContext = context
    }

    interface ProductFeaturesAboutItemPresenterProvider {
        fun getProductFeaturesAboutItemProvider(): ProductFeaturesAboutItemView.Presenter
    }

    companion object {

        private val ARG_PARAM_DATA = "param_data"

        fun newInstance(data: String): ProductFeaturesAboutItemFragment {
            val fragment = ProductFeaturesAboutItemFragment()

            val args = Bundle()
            args.putString(ARG_PARAM_DATA, data)
            fragment.arguments = args

            return fragment
        }
    }
}