package com.greenwallet.business.scenes.base

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.greenwallet.business.R
import com.greenwallet.business.scenes.login.LoginActivity

open class BaseActivity : AppCompatActivity() {

    companion object {
        const val FRAGMENT_LOADING = "fragment_loading"
    }

    fun replaceFragment(targetFragment: Fragment, tag: String) {
        val fragment = supportFragmentManager.findFragmentByTag(tag)
        if (fragment == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, targetFragment, tag)
                .addToBackStack(tag)
                .commit()
        } else if (!fragment.isVisible) {
            supportFragmentManager.popBackStack(tag, 0)
        }
    }

    fun userShouldReAuthenticate() {

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
    }
}