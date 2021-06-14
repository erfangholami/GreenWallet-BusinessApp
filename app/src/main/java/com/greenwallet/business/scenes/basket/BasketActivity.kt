package com.greenwallet.business.scenes.basket

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.greenwallet.business.databinding.ActivityBasketBinding
import com.greenwallet.business.scenes.base.BaseActivity
import com.greenwallet.business.scenes.basket.ui.BasketFeatureView
import com.greenwallet.business.scenes.basket.ui.BasketFragment

class BasketActivity : BaseActivity(), BasketFragment.BasketFeatureProvider,
    BasketProcessHandler {

    companion object {
        const val FRAGMENT_BASKET_FEATURE = "fragment_basket_feature"

        fun start(context: Context) {
            val intent = Intent(context, BasketActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var basketPresenter: BasketPresenter

    lateinit var binding: ActivityBasketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        basketPresenter = BasketPresenter(this)
        super.onCreate(savedInstanceState)
        binding = ActivityBasketBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        basketPresenter.subscribeHandler(this)
    }

    override fun onStop() {
        basketPresenter.disposeHandler()
        super.onStop()
    }

    override fun onBackPressed() {
        finish()
    }

    override fun getBasketFeatureProvider(): BasketFeatureView.Presenter {
        return basketPresenter
    }

    override fun showBasketFeatureScreen() {
        replaceFragment(BasketFragment(), FRAGMENT_BASKET_FEATURE)
    }

    override fun onProceedCheckoutCLick() {
        //TODO
//        CheckoutActivity.start(this)
    }
}