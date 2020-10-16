package com.greenwallet.business.helper.keystore;

public class CryptoFailedException extends RuntimeException {
    public CryptoFailedException(String message) {
        super(message);
    }

    public CryptoFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
