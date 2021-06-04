package com.greenwallet.business.scenes.onboarding

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.greenwallet.business.R
import com.greenwallet.business.app.Application
import com.greenwallet.business.helper.ui.LoadingFragment
import com.greenwallet.business.scenes.base.BaseActivity
import com.greenwallet.business.scenes.login.LoginActivity
import com.greenwallet.business.scenes.onboarding.ui.OnboardingFragment
import com.greenwallet.business.scenes.onboarding.ui.OnboardingView
import kotlinx.android.synthetic.main.activity_onboarding.*

class OnboardingActivity : BaseActivity(), OnboardingProcessHandler,
    OnboardingFragment.OnboardingPresenterProvider {

    companion object {
        const val FRAGMENT_ONBOARDING = "fragment_onboarding"
        const val FRAGMENT_LOADING = "fragment_loading"
    }

    lateinit var presenter: OnboardingPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        Application.context = this
        Application.app = application

        presenter = OnboardingPresenter(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

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

    override fun showOnboardingScreen() {
        toolbar.visibility = View.GONE

        val fragment = supportFragmentManager.findFragmentByTag(FRAGMENT_ONBOARDING)
        if (fragment == null) {

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, OnboardingFragment(), FRAGMENT_ONBOARDING)
                .addToBackStack(FRAGMENT_ONBOARDING)
                .commit()

        } else if (!fragment.isVisible) {
            supportFragmentManager.popBackStack(FRAGMENT_ONBOARDING, 0)
        }
    }

    override fun showLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)

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

    override fun getOnboardingPresenter(): OnboardingView.Presenter {
        return presenter
    }
}
