package com.greenwallet.business.scenes.login

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.greenwallet.business.R
import com.greenwallet.business.helper.ui.LoadingFragment
import com.greenwallet.business.scenes.login.ui.LoginFragment
import com.greenwallet.business.scenes.login.ui.LoginView
import com.greenwallet.business.scenes.shopgreen.ShopGreenActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginProcessHandler,
    LoginFragment.LoginPresenterProvider {

    companion object {
        const val FRAGMENT_LOGIN = "fragment_login"
        const val FRAGMENT_LOADING = "fragment_loading"
    }

    lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        presenter = LoginPresenter(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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

    override fun showLoginScreen() {
        toolbar.visibility = View.GONE

        val fragment = supportFragmentManager.findFragmentByTag(FRAGMENT_LOGIN)
        if (fragment == null) {

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, LoginFragment(), FRAGMENT_LOGIN)
                .addToBackStack(FRAGMENT_LOGIN)
                .commit()

        } else if (!fragment.isVisible) {
            supportFragmentManager.popBackStack(FRAGMENT_LOGIN, 0)
        }
    }

    override fun showShopGreenScreen() {
        val intent = Intent(this, ShopGreenActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

        startActivity(intent)
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
                showLoginScreen()
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

    override fun getLoginPresenter(): LoginView.Presenter {
        return presenter
    }
}
