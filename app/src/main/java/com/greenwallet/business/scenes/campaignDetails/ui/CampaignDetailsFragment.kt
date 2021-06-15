package com.greenwallet.business.scenes.campaignDetails.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.bumptech.glide.Glide
import com.greenwallet.business.R
import com.greenwallet.business.databinding.FragmentCampaignDetailsBinding
import com.greenwallet.business.helper.ui.ImageLoaderListener
import com.greenwallet.business.network.campaings.response.CampaignsResponseModel
import com.greenwallet.business.network.product.response.FileMode

class CampaignDetailsFragment(private val campaignDetails: CampaignsResponseModel) :
    Fragment(),
    CampaignDetailsView {

    private lateinit var presenter: CampaignDetailsView.Presenter

    private lateinit var binding: FragmentCampaignDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCampaignDetailsBinding.inflate(inflater, container, false)
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

        binding.llContent.background = GradientDrawable().apply {
            val corner =
                requireContext().resources.getDimensionPixelSize(R.dimen.corner_radius_default)
            setColor(requireContext().resources.getColor(R.color.white))
            cornerRadius = corner.toFloat()
        }

        binding.ivBackButton.setOnClickListener {
            presenter.onCloseButtonClicked()
        }

        if (!campaignDetails.defaultFileUrl.isNullOrEmpty()) {
            Glide.with(binding.root)
                .load(campaignDetails.defaultFileUrl)
                .centerCrop()
                .placeholder(R.drawable.bg_image_loading)
                .into(binding.ivImage)
        } else if (!campaignDetails.defaultFileId.isNullOrEmpty()) {
            presenter.fetchImage(campaignDetails.defaultFileId, object : ImageLoaderListener {
                override fun onFetchFinished(image: Bitmap?) {
                    binding.ivImage.setImageBitmap(image)
                    binding.root.requestLayout()
                }
            },
                reduceQuality = true,
                sizes = null)
        }

        binding.tvTitle.text = campaignDetails.name

        binding.tvAboutDescription.text = campaignDetails.details

        binding.tvNeedToDoDescription.text = campaignDetails.description

        val tagList = campaignDetails.tags?.split(',')?.toTypedArray() ?: arrayOf()
        val handleList = campaignDetails.handles?.split(',')?.toTypedArray() ?: arrayOf()

        binding.rvTags.layoutManager = GridLayoutManager(context, 2, VERTICAL, false)
        binding.rvTags.adapter = TagsAdapter(requireContext(), tagList + handleList).apply {
            itemClickListener = { tag ->
                presenter.onTagClicked(tag)
            }
        }

        binding.rvInspirations.layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
        binding.rvInspirations.adapter = InspirationsAdapter().apply {

            if (campaignDetails.inspirationUrls != null && campaignDetails.inspirationUrls!!.isNotEmpty()) {
                mode = FileMode.URL
                items = campaignDetails.inspirationUrls!!
            } else {
                mode = FileMode.ID
                items = campaignDetails.inspirationIds!!
            }

            imageLoadListener = { id, listener, sizes ->
                presenter.fetchImage(id, listener, sizes = sizes, true)
            }
        }

        binding.tvRulesDescription.text =
            "One post only every ${campaignDetails.postFrequency} hours." +
                    "\nOnly post within the past 24 hours on Instagram are counted." +
                    "\nNo promotional or advertising post, no influencers." +
                    "\nNo promotion of unethical and environment-damaging products." +
                    "\nPosts must be from public personal instagram account." +
                    "\nYour Instagram account must be Public not private."

        binding.clBottomContainer.visibility = View.GONE

        binding.btnLinkInstagram.setOnClickListener {
            presenter.onLinkInstagramButtonClicked()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is CampaignDetailsPresenterProvider) {
            this.presenter = context.getCampaignDetailsPresenter()
        } else {
            throw RuntimeException("$context must implement CampaignDetailsPresenterProvider")
        }
    }

    interface CampaignDetailsPresenterProvider {
        fun getCampaignDetailsPresenter(): CampaignDetailsView.Presenter
    }
}