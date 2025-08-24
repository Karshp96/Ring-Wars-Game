package com.unitbase.game.service;

import com.unitbase.game.model.GameState;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {
    private Map<String, GameState> games = new ConcurrentHashMap<>();

    public GameState createGame() {
        GameState game = new GameState();
        games.put(game.getGameId(), game);
        return game;
    }

    public GameState getGame(String gameId) {
        return games.get(gameId);
    }

    public GameState joinGame(String gameId, String playerName) {
        GameState game = games.get(gameId);
        if (game != null && game.addPlayer(playerName)) {
            return game;
        }
        return null;
    }

    public GameState makeMove(String gameId, int row, int col, String size, String playerColor) {
        GameState game = games.get(gameId);
        if (game != null && game.makeMove(row, col, size, playerColor)) {
            return game;
        }
        return null;
    }

    public void removeGame(String gameId) {
        games.remove(gameId);
    }
}