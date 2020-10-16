package com.greenwallet.business.helper.keystore.store;

import android.content.Context;
import android.util.Base64;

import androidx.annotation.Nullable;

public final class CipherPreferencesStorage implements Storage {
    private static final String SHARED_PREFERENCES_NAME = CipherPreferencesStorage.class.getName() + "_security_storage";

    private final Context context;
    private final String preferenceName;

    public CipherPreferencesStorage(Context context, String preferenceName) {
        this.context = context;
        this.preferenceName = preferenceName;
    }

    public CipherPreferencesStorage(Context context) {
        this(context, SHARED_PREFERENCES_NAME);
    }

    private void saveKeyString(String alias, String value) {
        context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
                .edit()
                .putString(alias, value)
                .apply();
    }

    @Nullable
    private String getKeyString(String alias) {
        return context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
                .getString(alias, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(String alias) {
        context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
                .edit()
                .remove(alias)
                .apply();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsAlias(String alias) {
        return context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
                .contains(alias);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public byte[] getKeyBytes(String alias) {
        String value = getKeyString(alias);
        if (value != null) {
            return Base64.decode(value, Base64.DEFAULT);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveKeyBytes(String alias, byte[] value) {
        saveKeyString(alias, Base64.encodeToString(value, Base64.DEFAULT));
    }
}
