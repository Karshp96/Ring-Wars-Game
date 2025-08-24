package com.unitbase.game.controller.intf;

import com.unitbase.game.model.GameState;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/game")
public interface IGameController {

    @GetMapping("/test")
    ResponseEntity<Map<String, String>> test();

    @PostMapping("/create")
    ResponseEntity<GameState> createGame();

    @PostMapping("/{gameId}/join")
    ResponseEntity<GameState> joinGame(@PathVariable String gameId,
                                              @RequestBody Map<String, String> request);

    @PostMapping("/{gameId}/move")
    ResponseEntity<GameState> makeMove(@PathVariable String gameId,
                                       @RequestBody Map<String, Object> move);

    @GetMapping("/{gameId}")
    ResponseEntity<GameState> getGame(@PathVariable String gameId);

}
