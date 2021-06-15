package com.greenwallet.business.scenes.campaignList

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.greenwallet.business.databinding.FragmentExploreCamnpaignsBinding

class ExploreCampaignsFragment : Fragment(), ExploreCampaignsView {

    private lateinit var presenter: ExploreCampaignsView.Presenter
    private lateinit var binding: FragmentExploreCamnpaignsBinding

    private var currentContext: Context? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExploreCamnpaignsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    override fun onStart() {
        super.onStart()
        presenter.subscribeView(this)

        setupCampaignList()
    }

    override fun onStop() {
        presenter.disposeView(this)
        super.onStop()
    }

    private fun setup() {
        binding.rvCampaignList.layoutManager = LinearLayoutManager(context)

        binding.ivBackButton.setOnClickListener {
            presenter.onBackButtonClicked()
        }
    }

    private fun setupCampaignList() {
        val campaignList = presenter.getCampaigns()

        if (campaignList.size > 0) {
            binding.rvCampaignList.visibility = View.VISIBLE
            binding.clNoCampaignContainer.visibility = View.GONE

            if (binding.rvCampaignList.adapter != null) {
                (binding.rvCampaignList.adapter as ExploreCampaignListAdapter).campaignList = campaignList
            } else {
                binding.rvCampaignList.adapter =
                    ExploreCampaignListAdapter().apply {
                        this.campaignList = campaignList
                        itemClickListener = { campaignModel ->
                            presenter.onCampaignClicked(campaignModel)
                        }
                        imageLoaderListener = { id, listener, sizes ->
                            presenter.fetchImage(id, listener, sizes, true)
                        }
                    }
            }
        } else {
            binding.rvCampaignList.visibility = View.GONE
            binding.clNoCampaignContainer.visibility = View.VISIBLE
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ExploreCampaignsPresenterProvider) {
            this.presenter = context.getExploreCampaignsPresenter()
        } else {
            throw RuntimeException("$context must implement ExploreCampaignsPresenterProvider")
        }

        currentContext = context
    }

    interface ExploreCampaignsPresenterProvider {
        fun getExploreCampaignsPresenter(): ExploreCampaignsView.Presenter
    }
}