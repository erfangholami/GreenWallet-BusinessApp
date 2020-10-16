package com.greenwallet.business.helper.keystore;

import androidx.annotation.Nullable;

/**
 * Main interface for access the KeyStore implementations
 */
public interface CipherStorage {
    /**
     * Encrypt the value associating with this alias
     * the alias it's the only way to access the value,
     * note if you already have the value stored this alias it will
     * override the current value
     *
     * @param alias the key for the value
     * @param value the value to store
     */
    void encrypt(String alias, String value);

    /**
     * Decrypt the value previously associated with this alias
     *
     * @param alias the key for access this value
     * @return null if no value has founded for this alias or the decrypted value
     */
    @Nullable
    String decrypt(String alias);

    /**
     * Check if there is an value for this alias stored,
     * including a check into the keystore and the Android Shared Preferences
     *
     * @param alias the key for access this value
     * @return true if there is a value associated with this key
     */
    boolean containsAlias(String alias);

    /**
     * Remove the current value previously associated with this key
     * it'll remove the value from the KeyStore and from the Android Shared Preferences
     *
     * @param alias the key for access this value
     */
    void removeKey(String alias);
}
