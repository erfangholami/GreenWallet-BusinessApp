package com.greenwallet.business.helper.keystore;

import android.content.Context;

import com.greenwallet.business.helper.keystore.store.Storage;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

abstract class BaseCipherStorage implements CipherStorage {
    static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    final Context context;
    final Storage storage;

    BaseCipherStorage(Context context, Storage storage) {
        this.context = context;
        this.storage = storage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsAlias(String alias) {
        try {
            KeyStore keyStore = getKeyStoreAndLoad();
            return keyStore.containsAlias(alias) &&
                    storage.containsAlias(alias);
        } catch (KeyStoreException e) {
            throw new KeyStoreAccessException("Failed to access Keystore", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeKey(String alias) {
        try {
            if (containsAlias(alias)) {
                KeyStore keyStore = getKeyStoreAndLoad();
                keyStore.deleteEntry(alias);
                storage.remove(alias);
            }
        } catch (KeyStoreException e) {
            throw new KeyStoreAccessException("Failed to access Keystore", e);
        }
    }

    static KeyStore getKeyStoreAndLoad() {
        try {
            KeyStore keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
            keyStore.load(null);
            return keyStore;
        } catch (NoSuchAlgorithmException | CertificateException | KeyStoreException | IOException e) {
            throw new KeyStoreAccessException("Could not access Keystore", e);
        }
    }
}
