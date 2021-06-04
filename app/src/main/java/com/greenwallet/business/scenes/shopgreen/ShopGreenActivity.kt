package com.greenwallet.business.scenes.shopgreen

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import com.greenwallet.business.R
import com.greenwallet.business.helper.ui.LoadingFragment
import com.greenwallet.business.scenes.base.BaseActivity
import com.greenwallet.business.scenes.shopgreen.ui.ShopGreenFragment
import com.greenwallet.business.scenes.shopgreen.ui.ShopGreenView
import kotlinx.android.synthetic.main.activity_login.*

class ShopGreenActivity : BaseActivity(), ShopGreenProcessHandler,
    ShopGreenFragment.ShopGreenPresenterProvider {

    companion object {
        const val FRAGMENT_SHOP_GREEN = "fragment_shop_green"
        const val FRAGMENT_LOADING = "fragment_loading"
    }

    lateinit var presenter: ShopGreenPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        presenter = ShopGreenPresenter(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_green)

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
        toolbar.visibility = View.GONE

        val fragment = supportFragmentManager.findFragmentByTag(FRAGMENT_SHOP_GREEN)
        if (fragment == null) {

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, ShopGreenFragment(), FRAGMENT_SHOP_GREEN)
                .addToBackStack(FRAGMENT_SHOP_GREEN)
                .commit()

        } else if (!fragment.isVisible) {
            supportFragmentManager.popBackStack(FRAGMENT_SHOP_GREEN, 0)
        }
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
