package com.greenwallet.business.scenes.checkout

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.greenwallet.business.databinding.ActivityCheckoutBinding
import com.greenwallet.business.helper.ui.LoadingFragment
import com.greenwallet.business.network.product.response.CartProduct
import com.greenwallet.business.scenes.base.BaseActivity
import com.greenwallet.business.scenes.checkout.ui.CheckoutFragment
import com.greenwallet.business.scenes.checkout.ui.CheckoutView

class CheckoutActivity : BaseActivity(), CheckoutProcessHandler,
    CheckoutFragment.CheckoutPresenterProvider {

    companion object {
        const val FRAGMENT_CHECKOUT_FEATURE = "fragment_checkout_feature"

        private const val KEY_MODE = "mode"

        fun start(context: Context, mode: Mode = Mode.ORDINARY) {
            val intent = Intent(context, CheckoutActivity::class.java)
            intent.putExtra(KEY_MODE, mode)
            context.startActivity(intent)
        }
    }

    enum class Mode {
        ORDINARY,
        BUY_NOW,
    }

    private lateinit var checkoutPresenter: CheckoutPresenter

    lateinit var binding: ActivityCheckoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        checkoutPresenter = CheckoutPresenter(this)
        checkoutPresenter.mode = intent.extras?.getSerializable(KEY_MODE) as Mode

        checkoutPresenter.fetchShipmentInformationFromServer()

        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        checkoutPresenter.subscribeHandler(this)
    }

    override fun onStop() {
        checkoutPresenter.disposeHandler()
        super.onStop()
    }

    override fun onBackPressed() {
        if (checkoutPresenter.state == CheckoutPresenter.State.CHECKOUT_FEATURES) {
            finish()
        }

        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }

    override fun showLoadingScreen() {
        replaceFragment(LoadingFragment.newInstance("Just a moment..."), FRAGMENT_LOADING)
    }

    override fun showErrorMessage() {

        showErrorDialog(listener = {
            it.dismiss()
            checkoutPresenter.onDismissError()
        })
    }

    override fun showShippingMethodSelectionScreen(productItem: CartProduct) {
        //todo
//        SelectShippingMethodActivity.start(
//            this,
//            productItem
//        )
    }

    override fun showCheckoutScreen() {
        replaceFragment(CheckoutFragment(), FRAGMENT_CHECKOUT_FEATURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            //todo
//            SelectShippingMethodActivity.SHIPPING_SELECTION_REQUEST_CODE -> {
//                if (resultCode == RESULT_OK) {
//                    val product: CartProduct = GsonBuilder().create().fromJson(
//                        data?.extras?.getString(SelectShippingMethodActivity.KEY_PRODUCT)!!,
//                        object : TypeToken<CartProduct>() {}.type
//                    )
//
//                    checkoutPresenter.itemDeliveryMethodChanged(product)
//
//                }
//            }
        }
    }

    override fun getCheckoutPresenter(): CheckoutView.Presenter {
        return checkoutPresenter
    }
}