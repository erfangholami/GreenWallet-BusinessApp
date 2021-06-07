package com.greenwallet.business.helper.keystore

import com.greenwallet.business.app.Application.Companion.context

class User {

    private val encryptedPreference =
        context.let { EncryptedPreference.getInstance(it, "ENCRYPTED_PREFERENCE_USER") }

    enum class Keys {
        MERCHANT_ID,
        EMAIL,
        PASSWORD,
        ACCESS_TOKEN,
    }

    companion object {
        val shared = User()
    }

    var merchantId: String? = null
        get() {
            return encryptedPreference.getString(Keys.MERCHANT_ID.toString(), "")
        }
        set(value) {
            field = value

            encryptedPreference.putString(Keys.MERCHANT_ID.toString(), value)
        }

    var email: String? = null
        get() {
            return encryptedPreference.getString(Keys.EMAIL.toString(), "")
        }
        set(value) {
            field = value

            encryptedPreference.putString(Keys.EMAIL.toString(), value)
        }

    var password: String? = null
        get() {
            return encryptedPreference.getString(Keys.PASSWORD.toString(), "")
        }
        set(value) {
            field = value

            encryptedPreference.putString(Keys.PASSWORD.toString(), value)
        }

}