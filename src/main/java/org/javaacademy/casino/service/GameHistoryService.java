package org.javaacademy.casino.service;

import lombok.RequiredArgsConstructor;
import org.javaacademy.casino.dto.GameHistoryDto;
import org.javaacademy.casino.dto.GameResultDto;
import org.javaacademy.casino.entity.FinanceResult;
import org.javaacademy.casino.entity.Game;
import org.javaacademy.casino.repository.FinanceResultRepository;
import org.javaacademy.casino.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameHistoryService {
    private static final int COUNT_LAST_GAMES = 5;
    private final FinanceResultRepository financeResultRepository;
    private final GameRepository gameRepository;

    public GameHistoryDto getHistory() {
        GameHistoryDto dto = new GameHistoryDto();
        FinanceResult financeResult = financeResultRepository.findResult().orElseThrow();
        dto.setPlayerIncome(financeResult.getIncome());
        dto.setPlayerOutcome(financeResult.getOutcome());
        List<Game> lastFiveGames = gameRepository.findAllByPortion(COUNT_LAST_GAMES);
        List<GameResultDto> gameResultDtos = lastFiveGames.stream().map(this::convertToDto).toList();
        dto.setGameHistory(gameResultDtos);
        return dto;
    }

    private GameResultDto convertToDto(Game entity) {
        GameResultDto gameResultDto = new GameResultDto();
        String combination = entity.getCombination();
        gameResultDto.setCombination(combination);
        gameResultDto.setWinIncome(GameCombination.getWinRate(combination));
        return gameResultDto;
    }
}