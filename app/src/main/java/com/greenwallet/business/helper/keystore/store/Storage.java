package com.greenwallet.business.helper.keystore.store;

import androidx.annotation.Nullable;

public interface Storage {
    /**
     * Responsible for save the current value associated with this alias
     *
     * @param alias   something unique to retrieve the value from the system
     * @param content the actual content of the key (encrypted)
     */
    void saveKeyBytes(String alias, byte[] content);

    /**
     * Return the current encrypted value of the current alias
     *
     * @param alias the unique alias to retrieve
     * @return the key bytes in success null otherwise
     */
    @Nullable
    byte[] getKeyBytes(String alias);

    /**
     * Check if the value for this alias exists
     *
     * @param alias the unique alias to check
     * @return true if the value exists
     */
    boolean containsAlias(String alias);

    /**
     * Remove the current value associated with this alias
     *
     * @param alias the unique alias to check
     */
    void remove(String alias);
}
