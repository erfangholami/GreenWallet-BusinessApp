package com.greenwallet.business.scenes.selectShipping

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.greenwallet.business.databinding.ActivitySelectShippingMethodBinding
import com.greenwallet.business.network.product.response.CartProduct
import com.greenwallet.business.scenes.base.BaseActivity
import com.greenwallet.business.scenes.selectShipping.ui.SelectShippingMethodFragment
import com.greenwallet.business.scenes.selectShipping.ui.SelectShippingMethodView

class SelectShippingMethodActivity : BaseActivity(), SelectShippingMethodProcessHandler,
    SelectShippingMethodFragment.SelectShippingMethodPresenterProvider {

    companion object {
        const val FRAGMENT_SELECT_SHIPPING_METHOD = "fragment_select_shipping_method"

        const val SHIPPING_SELECTION_REQUEST_CODE = 999

        const val KEY_PRODUCT = "product"

        fun start(
            activity: Activity,
            product: CartProduct
        ) {
            val intent = Intent(activity, SelectShippingMethodActivity::class.java)

            intent.putExtra(KEY_PRODUCT, GsonBuilder().create().toJson(product))

            activity.startActivityForResult(intent, SHIPPING_SELECTION_REQUEST_CODE)
        }
    }

    lateinit var presenter: SelectShippingMethodPresenter
    lateinit var binding: ActivitySelectShippingMethodBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        presenter = SelectShippingMethodPresenter(this)

        presenter.product = GsonBuilder().create().fromJson(
            intent.extras?.getString(KEY_PRODUCT, "{}"),
            object : TypeToken<CartProduct>() {}.type
        )

        super.onCreate(savedInstanceState)
        binding = ActivitySelectShippingMethodBinding.inflate(layoutInflater)
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

    override fun showShippingMethodSelectionScreen() {
        replaceFragment(SelectShippingMethodFragment(), FRAGMENT_SELECT_SHIPPING_METHOD)
    }

    override fun dismiss(product: CartProduct) {

        val intent = Intent()
        intent.putExtra(KEY_PRODUCT, GsonBuilder().create().toJson(product))

        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onBackPressed() {
        presenter.dismiss()
    }

    override fun getSelectShippingMethodPresenter(): SelectShippingMethodView.Presenter {
        return presenter
    }
}