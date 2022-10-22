package com.dreamgames.alihan.game.exception;

public class RewardNotClaimedException extends RuntimeException {
    public RewardNotClaimedException(String errorMessage) {
        super(errorMessage);
    }
}
