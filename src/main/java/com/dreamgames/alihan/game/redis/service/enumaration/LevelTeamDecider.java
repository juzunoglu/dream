package com.dreamgames.alihan.game.redis.service.enumaration;

import lombok.*;

@Getter
public enum LevelTeamDecider {

    LEVEL_1("GROUP1"),
    LEVEL_2("GROUP2"),
    LEVEL_3("GROUP3"),
    LEVEL_4("GROUP4"),
    LEVEL_5("GROUP5"),
    LEVEL_6("GROUP6"),
    LEVEL_7("GROUP7");

    private final String groupName;

    LevelTeamDecider(String groupName) {
        this.groupName = groupName;
    }
}
