package com.greenwallet.business.scenes.login.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextPaint
import android.text.TextWatcher
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.greenwallet.business.R
import com.greenwallet.business.databinding.FragmentLoginBinding
import com.greenwallet.business.helper.keystore.CipherStorageFactory
import com.greenwallet.business.helper.keystore.KeystoreKeys
import com.greenwallet.business.helper.kotlin.isValidEmail
import com.greenwallet.business.helper.kotlin.makeLinks

class LoginFragment : Fragment(), LoginView {

    private lateinit var presenter: LoginView.Presenter
    lateinit var binding: FragmentLoginBinding

    private var currentContext: Context? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
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
        val btnLogin = binding.btnLogin.btnDefault

        btnLogin.isEnabled = false
        btnLogin.alpha = 0.8f

        btnLogin.text = "Login"
        binding.tvDescription.text =
            "You must be a Green Wallet seller to login.\n\n\nNot a Green Wallet seller yet?\nVisit greenwallet.com/signup to create an account."

        btnLogin.setOnClickListener {
            Log.i("LoginFragment Fragment:", "Login button!")

            val cipherStorage = CipherStorageFactory.newInstance(context)
            cipherStorage.encrypt(KeystoreKeys.email.name, binding.etEmail.text.toString())
            cipherStorage.encrypt(KeystoreKeys.password.name, binding.etPassword.text.toString())

            presenter.onLoginButtonClicked(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )
        }

        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                checkLoginButtonStatus()

                if (!s.toString().isValidEmail() && s.toString().isNotEmpty()) {
                    binding.tiEmail.error = "Invalid email"
                } else {
                    binding.tiEmail.error = null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                checkLoginButtonStatus()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        setupClickableTextView()
        loadValuesIfAvailable()
    }

    private fun setupClickableTextView() {
        val signUpClick = object : ClickableSpan() {

            override fun onClick(widget: View) {
                val openURL = Intent(Intent.ACTION_VIEW)

                openURL.data =
                    Uri.parse("https://greenwallet.com/signup")
                startActivity(openURL)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false // set to false to remove underline
                ds.color = ContextCompat.getColor(context!!, R.color.colorAccent)
            }
        }

        makeLinks(binding.tvDescription, arrayOf("greenwallet.com/signup"), arrayOf(signUpClick))
    }

    private fun loadValuesIfAvailable() {
        val cipherStorage = CipherStorageFactory.newInstance(context)

        if (!cipherStorage.decrypt(KeystoreKeys.email.name).isNullOrEmpty()) {
            binding.etEmail.setText(cipherStorage.decrypt(KeystoreKeys.email.name))
        }

        if (!cipherStorage.decrypt(KeystoreKeys.password.name).isNullOrEmpty()) {
            binding.etPassword.setText(cipherStorage.decrypt(KeystoreKeys.password.name))
        }
    }

    private fun checkLoginButtonStatus() {
        val btnLogin = binding.btnLogin.btnDefault

        btnLogin.isEnabled = binding.etEmail.text.toString().isNotEmpty() &&
                binding.etEmail.text.toString().isValidEmail() &&
                binding.etPassword.text.toString().isNotEmpty()

        btnLogin.alpha = if (btnLogin.isEnabled) 1.0f else 0.8f
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is LoginPresenterProvider) {
            this.presenter = context.getLoginPresenter()
        } else {
            throw RuntimeException("$context must implement LoginPresenterProvider")
        }

        currentContext = context
    }

    interface LoginPresenterProvider {
        fun getLoginPresenter(): LoginView.Presenter
    }
}
