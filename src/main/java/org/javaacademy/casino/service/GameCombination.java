package org.javaacademy.casino.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum GameCombination {
    AAA(10, false),
    FFF(20, false),
    DDD(50, false),
    AFD(null, true);

    private final Integer winRate;
    private final boolean isFreeGame;

    public static boolean isFreeGameCombination(String combination) {
        return Arrays.stream(GameCombination.values())
                .filter(e -> e.isFreeGame)
                .anyMatch(e -> e.name().equals(combination));
    }

    public static boolean isWinGameCombination(String combination) {
        return Arrays.stream(GameCombination.values())
                .anyMatch(e -> e.name().equals(combination)
                               && e.getWinRate() != null
                               && e.getWinRate() > 0);
    }

    public static Integer getWinRate(String combination) {
        return Arrays.stream(GameCombination.values())
                .filter(e -> e.name().equals(combination))
                .map(GameCombination::getWinRate)
                .findFirst()
                .orElse(0);
    }
}