package com.dreamgames.alihan.game.exception;

public class RewardAlreadyClaimedException extends RuntimeException {

    public RewardAlreadyClaimedException(String errorMessage) {
        super(errorMessage);
    }
}
