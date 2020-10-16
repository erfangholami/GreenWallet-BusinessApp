package com.greenwallet.business.scenes.onboarding.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.greenwallet.business.R
import kotlinx.android.synthetic.main.fragment_onboarding.*

class OnboardingFragment : Fragment(), OnboardingView {

    private lateinit var presenter: OnboardingView.Presenter

    private var currentContext: Context? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_onboarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setup()
    }

    override fun onStart() {
        super.onStart()
        presenter.subscribeView(this)
    }

    override fun onStop() {
        presenter.disposeView(this)
        super.onStop()
    }

    private fun setup() {
        val btnLogin = btn_login.findViewById<Button>(R.id.btn_default)

        btnLogin.text = "Login"

        btnLogin.setOnClickListener {
            Log.i("Onboarding Fragment:", "Login button!")

            presenter.onLoginButtonClicked()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnboardingPresenterProvider) {
            this.presenter = context.getOnboardingPresenter()
        } else {
            throw RuntimeException("$context must implement OnboardingPresenterProvider")
        }

        currentContext = context
    }

    interface OnboardingPresenterProvider {
        fun getOnboardingPresenter(): OnboardingView.Presenter
    }
}
