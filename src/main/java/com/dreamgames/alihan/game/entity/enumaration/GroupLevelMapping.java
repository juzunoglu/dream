package com.dreamgames.alihan.game.entity.enumaration;

import lombok.Getter;

@Getter
public enum GroupLevelMapping {

    GROUP1(100),
    GROUP2(200),
    GROUP3(300),
    GROUP4(400),
    GROUP5(500),
    GROUP6(600),
    GROUP7(700),
    GROUP8(800);
    private final Integer levelLimit;
    GroupLevelMapping(Integer levelLimit) {
        this.levelLimit = levelLimit;
    }
}
