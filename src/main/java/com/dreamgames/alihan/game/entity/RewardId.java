package com.dreamgames.alihan.game.entity;

import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RewardId implements Serializable {

    private Long tournament;
    private Long user;
}
