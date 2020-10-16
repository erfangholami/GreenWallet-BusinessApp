package com.greenwallet.business.helper.keystore;

import android.content.Context;
import android.os.Build;

import com.greenwallet.business.helper.keystore.store.CipherPreferencesStorage;
import com.greenwallet.business.helper.keystore.store.Storage;

/**
 * Factory class for {@link CipherStorage} it'll decide what
 * is the best implementation based on the current api level
 *
 * @see #newInstance(Context, Storage)
 */
public final class CipherStorageFactory {
    private CipherStorageFactory() {
        throw new AssertionError();
    }

    /**
     * Create a new instance of the {@link CipherStorage} based on the
     * current api level, on API 22 and bellow it will use the {@link CipherStorageAndroidKeystore}
     * and on api 23 and above it will use the {@link CipherStorageSharedPreferencesKeystore}
     *
     * @param context used for api 22 and bellow to access the keystore and
     *                access the Android Shared preferences, on api 23 and above
     *                it's only used for Android Shared Preferences access
     *
     * @param storage abstraction for store the key and value bytes into the system
     *                you can implement your own version of the storage to fit your needs
     * @return a new {@link CipherStorage} based on the current api level
     */
    public static CipherStorage newInstance(Context context, Storage storage) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return new CipherStorageAndroidKeystore(context, storage);
        }
        return new CipherStorageSharedPreferencesKeystore(context, storage);
    }

    /**
     * @see #newInstance(Context, Storage)
     */
    public static CipherStorage newInstance(Context context) {
        return newInstance(context, new CipherPreferencesStorage(context));
    }
}
