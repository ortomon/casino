package org.javaacademy.casino.controller;

import lombok.RequiredArgsConstructor;
import org.javaacademy.casino.dto.GameHistoryDto;
import org.javaacademy.casino.service.GameHistoryService;
import org.javaacademy.casino.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;
    private final GameHistoryService gameHistoryService;

    @PostMapping("/play")
    public ResponseEntity<String> play() {
        return ResponseEntity.status(CREATED).body(gameService.playGame());
    }

    @GetMapping("/history")
    public GameHistoryDto getHistory() {
        return gameHistoryService.getHistory();
    }

}