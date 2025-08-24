package com.unitbase.game.controller;

import com.unitbase.game.controller.intf.IGameController;
import com.unitbase.game.model.GameState;
import com.unitbase.game.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
public class GameController implements IGameController {

    @Autowired
    private GameService gameService;

    // Test endpoint to verify server is running
    public ResponseEntity<Map<String, String>> test() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "Server is running!");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<GameState> createGame() {
        try {
            System.out.println("Creating new game...");
            GameState game = gameService.createGame();
            System.out.println("Game created with ID: " + game.getGameId());
            return ResponseEntity.ok(game);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<GameState> joinGame(@PathVariable String gameId,
                                              @RequestBody Map<String, String> request) {
        try {
            String playerName = request.get("playerName");
            System.out.println("Player " + playerName + " joining game " + gameId);
            GameState game = gameService.joinGame(gameId, playerName);

            if (game != null) {
                System.out.println("Player joined successfully. Players: " + game.getPlayers().size());
                return ResponseEntity.ok(game);
            } else {
                System.out.println("Failed to join game");
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<GameState> makeMove(@PathVariable String gameId,
                                              @RequestBody Map<String, Object> move) {
        try {
            int row = (Integer) move.get("row");
            int col = (Integer) move.get("col");
            String size = (String) move.get("size");
            String playerColor = (String) move.get("playerColor");

            System.out.println("Move attempt: " + playerColor + " " + size + " at " + row + "," + col);

            GameState game = gameService.makeMove(gameId, row, col, size, playerColor);

            if (game != null) {
                System.out.println("Move successful. Status: " + game.getStatus());
                return ResponseEntity.ok(game);
            } else {
                System.out.println("Move failed");
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<GameState> getGame(@PathVariable String gameId) {
        try {
            GameState game = gameService.getGame(gameId);

            if (game != null) {
                return ResponseEntity.ok(game);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}