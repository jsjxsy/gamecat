package com.youximao.sdk.lib.common.common.util.AES;

/**
 * An unexpected exception
 */
public class CryptoException extends Exception {

    public CryptoException(String message) {
        super(message);
    }

    public CryptoException(Throwable exception) {
        super("Unexpected Error", exception);
    }

    public CryptoException(String message, Throwable cause) {
        super(message, cause);
    }

}

