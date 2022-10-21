package com.dreamgames.alihan.game.exception;

public class InsufficientCoinException extends RuntimeException {

    public InsufficientCoinException(String errorMessage) {
        super(errorMessage);
    }

}
