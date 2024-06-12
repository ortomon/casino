package org.javaacademy.casino.service;

import lombok.RequiredArgsConstructor;
import org.javaacademy.casino.entity.Game;
import org.javaacademy.casino.repository.FinanceResultRepository;
import org.javaacademy.casino.repository.GameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.Random;
import java.util.stream.IntStream;

import static java.math.BigDecimal.valueOf;
import static java.util.stream.Collectors.joining;
import static org.javaacademy.casino.service.GameCombination.getWinRate;
import static org.javaacademy.casino.service.GameCombination.isFreeGameCombination;
import static org.javaacademy.casino.service.GameCombination.isWinGameCombination;

@Service
@RequiredArgsConstructor
public class GameService {
    private static final int COUNT_SYMBOL_IN_ONE_GAME = 3;
    private static final BigDecimal PAY_FOR_ONE_GAME = valueOf(15);
    private final Random random = new Random(1);
    private final TransactionTemplate transactionTemplate;
    private final FinanceResultRepository financeResultRepository;
    private final GameRepository gameRepository;

    public String playGame() {
        return transactionTemplate.execute(this::playGame);
    }

    private String playGame(TransactionStatus transactionStatus) {
        String combination = getCombination();
        String[] gameSymbols = combination.split("");
        Game game = new Game(gameSymbols[0], gameSymbols[1], gameSymbols[2]);
        gameRepository.create(game);

        Object savepoint = transactionStatus.createSavepoint();
        financeResultRepository.addOutcome(PAY_FOR_ONE_GAME);
        if (isFreeGameCombination(combination)) {
            transactionStatus.rollbackToSavepoint(savepoint);
            return "Бесплатный ход";
        }
        if (isWinGameCombination(combination)) {
            Integer winRate = getWinRate(combination);
            financeResultRepository.addIncome(valueOf(winRate));
            return "Вы выиграли - " + winRate;
        }
        return "Вы проиграли";
    }

    private String getCombination() {
        return IntStream.range(0, COUNT_SYMBOL_IN_ONE_GAME)
                .boxed()
                .map(e -> GameSymbol.values()[random.nextInt(GameSymbol.values().length)].name())
                .collect(joining());
    }
}