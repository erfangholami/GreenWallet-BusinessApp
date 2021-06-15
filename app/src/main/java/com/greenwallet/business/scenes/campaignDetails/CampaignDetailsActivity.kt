package com.greenwallet.business.scenes.campaignDetails

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.greenwallet.business.R
import com.greenwallet.business.databinding.ActivityCampaignDetailsBinding
import com.greenwallet.business.helper.ui.LoadingFragment
import com.greenwallet.business.helper.ui.dialog.ErrorDialog
import com.greenwallet.business.network.campaings.response.CampaignsResponseModel
import com.greenwallet.business.scenes.base.BaseActivity
import com.greenwallet.business.scenes.campaignDetails.ui.CampaignDetailsFragment
import com.greenwallet.business.scenes.campaignDetails.ui.CampaignDetailsView

class CampaignDetailsActivity : BaseActivity(),
    CampaignDetailsProcessHandler,
    CampaignDetailsFragment.CampaignDetailsPresenterProvider {

    companion object {
        const val FRAGMENT_CAMPAIGN_DETAILS = "fragment_campaign_details"
        const val CAMPAIGN_DETAIL_KEY = "campaign_details"
    }

    lateinit var presenter: CampaignDetailsPresenter
    lateinit var binding: ActivityCampaignDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        presenter = CampaignDetailsPresenter(this)

        super.onCreate(savedInstanceState)
        binding = ActivityCampaignDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setHomeButtonEnabled(false)

        if (intent.extras?.getSerializable(CAMPAIGN_DETAIL_KEY) != null) {
            presenter.campaignDetails =
                intent.extras?.getSerializable(CAMPAIGN_DETAIL_KEY) as CampaignsResponseModel
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.subscribeHandler(this)
    }

    override fun onStop() {
        presenter.disposeHandler()
        super.onStop()
    }

    override fun showCampaignDetailsScreen() {
        replaceFragment(
            CampaignDetailsFragment(presenter.campaignDetails),
            FRAGMENT_CAMPAIGN_DETAILS
        )
    }

    override fun showLinkInstagramScreen() {
        //todo
//        val intent = Intent(this, InstagramActivity::class.java)
//
//        startActivity(intent)
    }

    override fun dismiss() {
        finish()
    }

    override fun showLoadingScreen() {
        replaceFragment(
            LoadingFragment.newInstance("Just a moment..."),
            FRAGMENT_LOADING
        )
    }

    override fun showErrorMessage() {
        ErrorDialog(this).apply {
            buttonListener = {
                dismiss()
                //todo: Implement send request again
            }
        }.show()
    }

    override fun onTagClicked(tag: String) {
        val clipboard: ClipboardManager =
            getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Campaign tag", tag)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, resources.getString(R.string.tag_copied), Toast.LENGTH_SHORT).show()
    }

    override fun getCampaignDetailsPresenter(): CampaignDetailsView.Presenter {
        return presenter
    }
}