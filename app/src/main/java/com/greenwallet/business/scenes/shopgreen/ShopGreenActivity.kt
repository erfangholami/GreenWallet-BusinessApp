package com.greenwallet.business.scenes.shopgreen

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import com.greenwallet.business.R
import com.greenwallet.business.databinding.ActivityShopGreenBinding
import com.greenwallet.business.helper.ui.LoadingFragment
import com.greenwallet.business.scenes.base.BaseActivity
import com.greenwallet.business.scenes.shopgreen.ui.ShopGreenFragment
import com.greenwallet.business.scenes.shopgreen.ui.ShopGreenView

class ShopGreenActivity : BaseActivity(), ShopGreenProcessHandler,
    ShopGreenFragment.ShopGreenPresenterProvider {

    companion object {
        const val FRAGMENT_SHOP_GREEN = "fragment_shop_green"
        const val FRAGMENT_LOADING = "fragment_loading"
    }

    lateinit var presenter: ShopGreenPresenter
    lateinit var binding: ActivityShopGreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        presenter = ShopGreenPresenter(this)

        super.onCreate(savedInstanceState)
        binding = ActivityShopGreenBinding.inflate(layoutInflater)
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

    override fun showShopGreenScreen() {
        binding.toolbar.visibility = View.GONE

        replaceFragment(ShopGreenFragment(), FRAGMENT_SHOP_GREEN)
    }

    override fun showLoadingScreen() {
        if (supportFragmentManager.findFragmentByTag(FRAGMENT_LOADING)?.isVisible == true) {
            return
        }

        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.container,
                LoadingFragment.newInstance("Just a moment..."),
                FRAGMENT_LOADING
            )
            .addToBackStack(FRAGMENT_LOADING)
            .commit()
    }

    override fun showErrorMessage() {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage("Something went wrong!")
            .setPositiveButton(
                "Dismiss"
            ) { dialog, which ->

            }
            .setCancelable(false)
            .show()
    }

    override fun onBackPressed() {
        actionBackButton()
    }

    private fun actionBackButton() {
        if (supportFragmentManager.findFragmentByTag(FRAGMENT_LOADING)?.isVisible == true) {
            return
        }

        val backStackCount = supportFragmentManager.backStackEntryCount - 1
        if (backStackCount <= 0) {
            return
        }
        val entry = supportFragmentManager.getBackStackEntryAt(backStackCount)

        when {
            else -> return
        }
    }

    override fun getShopGreenPresenter(): ShopGreenView.Presenter {
        return presenter
    }
}
