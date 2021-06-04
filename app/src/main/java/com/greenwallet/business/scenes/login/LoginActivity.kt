package com.greenwallet.business.scenes.login

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.greenwallet.business.R
import com.greenwallet.business.databinding.ActivityLoginBinding
import com.greenwallet.business.helper.ui.LoadingFragment
import com.greenwallet.business.scenes.base.BaseActivity
import com.greenwallet.business.scenes.login.ui.LoginFragment
import com.greenwallet.business.scenes.login.ui.LoginView
import com.greenwallet.business.scenes.shopgreen.ShopGreenActivity

class LoginActivity : BaseActivity(), LoginProcessHandler,
    LoginFragment.LoginPresenterProvider {

    companion object {
        const val FRAGMENT_LOGIN = "fragment_login"
        const val FRAGMENT_LOADING = "fragment_loading"
    }

    lateinit var presenter: LoginPresenter
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        presenter = LoginPresenter(this)

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
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

    override fun showLoginScreen() {
        binding.toolbar.visibility = View.GONE

        replaceFragment(LoginFragment(), FRAGMENT_LOGIN)
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
