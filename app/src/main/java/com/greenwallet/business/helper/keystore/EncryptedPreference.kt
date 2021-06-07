package com.greenwallet.business.helper.keystore

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.greenwallet.business.BuildConfig
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.security.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

open class EncryptedPreference @Throws(SecurePreferencesException::class)
constructor(
    context: Context, preferenceName: String,
    secureKey: String, encryptKeys: Boolean
) {

    private val encryptKeys: Boolean
    private val writer: Cipher
    private val reader: Cipher
    private val keyWriter: Cipher
    private val preferences: SharedPreferences

    private val iv: IvParameterSpec
        get() {
            val iv = ByteArray(writer.blockSize)
            System.arraycopy(
                "fldsjfodasjifudslfjdsaofshaufihadsf".toByteArray(), 0,
                iv, 0, writer.blockSize
            )
            return IvParameterSpec(iv)
        }

    class SecurePreferencesException(e: Throwable) : RuntimeException(e)

    init {
        try {
            this.writer = Cipher.getInstance(TRANSFORMATION)
            this.reader = Cipher.getInstance(TRANSFORMATION)
            this.keyWriter = Cipher.getInstance(KEY_TRANSFORMATION)

            initCiphers(secureKey)

            this.preferences = context.getSharedPreferences(
                preferenceName,
                Context.MODE_PRIVATE
            )

            this.encryptKeys = encryptKeys
        } catch (e: GeneralSecurityException) {
            throw SecurePreferencesException(e)
        } catch (e: UnsupportedEncodingException) {
            throw SecurePreferencesException(e)
        }
    }

    @Throws(
        UnsupportedEncodingException::class,
        NoSuchAlgorithmException::class,
        InvalidKeyException::class,
        InvalidAlgorithmParameterException::class
    )
    protected fun initCiphers(secureKey: String) {
        val ivSpec = iv
        val secretKey = getSecretKey(secureKey)

        writer.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)
        reader.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)
        keyWriter.init(Cipher.ENCRYPT_MODE, secretKey)
    }

    @Throws(UnsupportedEncodingException::class, NoSuchAlgorithmException::class)
    protected fun getSecretKey(key: String): SecretKeySpec {
        val keyBytes = createKeyBytes(key)
        return SecretKeySpec(keyBytes, TRANSFORMATION)
    }

    @Throws(UnsupportedEncodingException::class, NoSuchAlgorithmException::class)
    protected fun createKeyBytes(key: String): ByteArray {
        val md = MessageDigest
            .getInstance(SECRET_KEY_HASH_TRANSFORMATION)
        md.reset()
        return md.digest(key.toByteArray(charset(CHARSET)))
    }

    fun putString(key: String, value: String?) {
        if (value == null) {
            preferences.edit().remove(toKey(key)).apply()
        } else {
            putValue(toKey(key), value)
        }
    }

    fun putBoolean(key: String, value: Boolean?) {
        if (value == null) {
            preferences.edit().remove(toKey(key)).apply()
        } else {
            putValue(toKey(key), java.lang.Boolean.toString(value))
        }
    }

    fun putLong(key: String, value: Long) {
        putValue(toKey(key), value.toString())

    }

    fun putInt(key: String, value: Int) {
        putValue(toKey(key), value.toString())
    }

    fun putStringList(key: String, array: ArrayList<String>) {

        putValue(toKey(key), GsonBuilder().create().toJson(array))
    }

    fun containsKey(key: String): Boolean {
        return preferences.contains(toKey(key))
    }

    fun removeValue(key: String) {
        preferences.edit().remove(toKey(key)).apply()
    }

    @Throws(SecurePreferencesException::class)
    fun getString(key: String, value: String): String {
        if (preferences.contains(toKey(key))) {
            val securedEncodedValue = preferences.getString(toKey(key), "")
            return decrypt(securedEncodedValue!!)
        }
        return value
    }

    @Throws(SecurePreferencesException::class)
    fun getLong(key: String, value: Long): Long {
        if (preferences.contains(toKey(key))) {
            val securedEncodedValue = preferences.getString(toKey(key), "")
            return java.lang.Long.parseLong(decrypt(securedEncodedValue!!))
        }
        return value
    }

    @Throws(SecurePreferencesException::class)
    fun getBoolean(key: String, value: Boolean): Boolean {
        if (preferences.contains(toKey(key))) {
            val securedEncodedValue = preferences.getString(toKey(key), "")
            return java.lang.Boolean.parseBoolean(decrypt(securedEncodedValue!!))
        }
        return value
    }

    @Throws(SecurePreferencesException::class)
    fun getBoolean(key: String): Boolean? {
        if (preferences.contains(toKey(key))) {
            val securedEncodedValue = preferences.getString(toKey(key), "")
            return java.lang.Boolean.parseBoolean(decrypt(securedEncodedValue!!))
        }
        return null
    }

    @Throws(SecurePreferencesException::class)
    fun getInt(key: String, value: Int): Int {
        if (preferences.contains(toKey(key))) {
            val securedEncodedValue = preferences.getString(toKey(key), "")
            return Integer.parseInt(decrypt(securedEncodedValue!!))
        }
        return value
    }

    @Throws(SecurePreferencesException::class)
    fun getStringList(key: String, value: ArrayList<String>): ArrayList<String> {
        if (preferences.contains(toKey(key))) {
            return GsonBuilder().create().fromJson<ArrayList<String>>(
                decrypt(preferences.getString(toKey(key), "[]")!!),
                object : TypeToken<ArrayList<String>>() {}.type
            )
        }
        return value
    }

    fun commit() {
        preferences.edit().apply()
    }

    fun clear() {
        preferences.edit().clear().apply()
    }

    private fun toKey(key: String): String {
        return if (encryptKeys)
            encrypt(key, keyWriter)
        else
            key
    }

    @Throws(SecurePreferencesException::class)
    private fun putValue(key: String, value: String) {
        val secureValueEncoded = encrypt(value, writer)

        preferences.edit().putString(key, secureValueEncoded).apply()
    }

    @Throws(SecurePreferencesException::class)
    protected fun encrypt(value: String, writer: Cipher): String {
        val secureValue: ByteArray
        try {
            secureValue = convert(writer, value.toByteArray(charset(CHARSET)))
        } catch (e: UnsupportedEncodingException) {
            throw SecurePreferencesException(e)
        }

        return Base64.encodeToString(
            secureValue,
            Base64.NO_WRAP
        )
    }

    private fun decrypt(securedEncodedValue: String): String {
        val securedValue = Base64
            .decode(securedEncodedValue, Base64.NO_WRAP)
        val value = convert(reader, securedValue)
        try {
            return String(value, Charset.forName(CHARSET))
        } catch (e: UnsupportedEncodingException) {
            throw SecurePreferencesException(e)
        }
    }

    companion object {
        private val TRANSFORMATION = "AES/CBC/PKCS5Padding"
        private val KEY_TRANSFORMATION = "AES/ECB/PKCS5Padding"
        private val SECRET_KEY_HASH_TRANSFORMATION = "SHA-256"
        private val CHARSET: String = "UTF-8"
        private val LIST_SEPERATOR: String = " */* "
        private var instance: EncryptedPreference? = null

        fun getInstance(context: Context, prefName: String): EncryptedPreference {
            if (instance == null) {
                instance = EncryptedPreference(
                    context, prefName,
                    BuildConfig.SECURE_KEY, true
                )
            }

            return instance as EncryptedPreference
        }

        @Throws(SecurePreferencesException::class)
        private fun convert(cipher: Cipher, bs: ByteArray): ByteArray {
            try {
                return cipher.doFinal(bs)
            } catch (e: Exception) {
                throw SecurePreferencesException(e)
            }
        }
    }
}