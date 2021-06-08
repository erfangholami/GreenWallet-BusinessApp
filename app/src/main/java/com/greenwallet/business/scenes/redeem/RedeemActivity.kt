package com.greenwallet.business.scenes.redeem

import android.os.Bundle
import com.greenwallet.business.databinding.ActivityRedeemBinding
import com.greenwallet.business.helper.ui.LoadingFragment
import com.greenwallet.business.network.product.response.ProductResponseModel
import com.greenwallet.business.scenes.base.BaseActivity
import com.greenwallet.business.scenes.redeem.ui.RedeemFragment
import com.greenwallet.business.scenes.redeem.ui.RedeemView

class RedeemActivity : BaseActivity(),
    RedeemProcessHandler,
    RedeemFragment.RedeemPresenterProvider {

    companion object {
        const val FRAGMENT_REDEEM = "fragment_redeem"
    }

    private lateinit var binding: ActivityRedeemBinding

    lateinit var presenter: RedeemPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        presenter = RedeemPresenter(this)

        super.onCreate(savedInstanceState)
        binding = ActivityRedeemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setHomeButtonEnabled(false)
    }

    override fun onStart() {
        super.onStart()
        presenter.subscribeHandler(this)
    }

    override fun onStop() {
        presenter.disposeHandler()
        super.onStop()
    }

    override fun showRedeemScreen() {
        replaceFragment(RedeemFragment(), FRAGMENT_REDEEM)
    }

    override fun dismiss() {
        finish()
    }

    override fun showLoadingScreen() {
        replaceFragment(LoadingFragment.newInstance("Just a moment..."), FRAGMENT_LOADING)
    }

    override fun showErrorMessage() {

    }

    override fun showProductDetailsScreen(selectedProduct: ProductResponseModel) {
        //todo: After adding product details
//        ProductFeaturesActivity.start(this, selectedProduct)
    }

    override fun shoeCartScreen() {
        //todo: After adding cart page
//        BasketActivity.start(this)
    }

    override fun onBackPressed() {
        dismiss()
    }

    override fun getRedeemPresenter(): RedeemView.Presenter {
        return presenter
    }
}