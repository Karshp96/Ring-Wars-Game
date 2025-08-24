package com.unitbase.game.controller;

import com.unitbase.game.model.GameState;
import com.unitbase.game.model.Player;
import com.unitbase.game.service.GameService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {

    @Mock
    private GameService gameService;

    @InjectMocks
    private GameController gameController;

    private GameState mockGameState;
    private List<Player> players;

    @BeforeEach
    void setUp() {
        players = new ArrayList<>();
        Player player1 = new Player("player1", "RED");
        players.add(player1);

        mockGameState = new GameState();
        mockGameState.addPlayer(player1.getName());
    }

    // ========== TEST() METHOD TESTS ==========

    @Test
    void test_ShouldReturnSuccessResponse() {
        // When
        ResponseEntity<Map<String, String>> response = gameController.test();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Server is running!", response.getBody().get("status"));
        assertNotNull(response.getBody().get("timestamp"));
        assertTrue(response.getBody().containsKey("timestamp"));
    }

    // ========== CREATEGAME() METHOD TESTS ==========

    @Test
    void createGame_Success_ShouldReturnGameState() {
        // Given
        when(gameService.createGame()).thenReturn(mockGameState);

        // When
        ResponseEntity<GameState> response = gameController.createGame();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mockGameState.getGameId(), response.getBody().getGameId());
        assertEquals("WAITING", response.getBody().getStatus());
        verify(gameService, times(1)).createGame();
    }

    @Test
    void createGame_Exception_ShouldReturnInternalServerError() {
        // Given
        when(gameService.createGame()).thenThrow(new RuntimeException("Database error"));

        // When
        ResponseEntity<GameState> response = gameController.createGame();

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(gameService, times(1)).createGame();
    }

    // ========== JOINGAME() METHOD TESTS ==========

    @Test
    void joinGame_Success_ShouldReturnGameState() {
        // Given
        String gameId = mockGameState.getGameId();
        String playerName = "player2";
        Map<String, String> request = new HashMap<>();
        request.put("playerName", playerName);

        players = new ArrayList<>();
        players.add(new Player(playerName, "BLUE"));
        mockGameState.addPlayer(playerName);

        when(gameService.joinGame(gameId, playerName)).thenReturn(mockGameState);

        // When
        ResponseEntity<GameState> response = gameController.joinGame(gameId, request);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getPlayers().size());
        verify(gameService, times(1)).joinGame(gameId, playerName);
    }

    @Test
    void joinGame_Failed_ShouldReturnBadRequest() {
        // Given
        String gameId = "test-game-id-fail";
        String playerName = "player2";
        Map<String, String> request = new HashMap<>();
        request.put("playerName", playerName);

        when(gameService.joinGame(gameId, playerName)).thenReturn(null);

        // When
        ResponseEntity<GameState> response = gameController.joinGame(gameId, request);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(gameService, times(1)).joinGame(gameId, playerName);
    }

    @Test
    void joinGame_Exception_ShouldReturnInternalServerError() {
        // Given
        String gameId = "test-game-id-error";
        String playerName = "player2";
        Map<String, String> request = new HashMap<>();
        request.put("playerName", playerName);

        when(gameService.joinGame(gameId, playerName)).thenThrow(new RuntimeException("Service error"));

        // When
        ResponseEntity<GameState> response = gameController.joinGame(gameId, request);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(gameService, times(1)).joinGame(gameId, playerName);
    }

    // ========== MAKEMOVE() METHOD TESTS ==========

    @Test
    void makeMove_Success_ShouldReturnGameState() {
        // Given
        String gameId = mockGameState.getGameId();
        Map<String, Object> move = new HashMap<>();
        move.put("row", 1);
        move.put("col", 2);
        move.put("size", "LARGE");
        move.put("playerColor", "RED");

        mockGameState.setStatus("IN_PROGRESS");

        when(gameService.makeMove(gameId, 1, 2, "LARGE", "RED")).thenReturn(mockGameState);

        // When
        ResponseEntity<GameState> response = gameController.makeMove(gameId, move);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("IN_PROGRESS", response.getBody().getStatus());
        verify(gameService, times(1)).makeMove(gameId, 1, 2, "LARGE", "RED");
    }

    @Test
    void makeMove_Failed_ShouldReturnBadRequest() {
        // Given
        String gameId = mockGameState.getGameId();
        Map<String, Object> move = new HashMap<>();
        move.put("row", 1);
        move.put("col", 2);
        move.put("size", "LARGE");
        move.put("playerColor", "RED");

        when(gameService.makeMove(gameId, 1, 2, "LARGE", "RED")).thenReturn(null);

        // When
        ResponseEntity<GameState> response = gameController.makeMove(gameId, move);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(gameService, times(1)).makeMove(gameId, 1, 2, "LARGE", "RED");
    }

    @Test
    void makeMove_Exception_ShouldReturnInternalServerError() {
        // Given
        String gameId = mockGameState.getGameId();
        Map<String, Object> move = new HashMap<>();
        move.put("row", 1);
        move.put("col", 2);
        move.put("size", "LARGE");
        move.put("playerColor", "RED");

        when(gameService.makeMove(gameId, 1, 2, "LARGE", "RED"))
                .thenThrow(new RuntimeException("Invalid move"));

        // When
        ResponseEntity<GameState> response = gameController.makeMove(gameId, move);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(gameService, times(1)).makeMove(gameId, 1, 2, "LARGE", "RED");
    }

    // ========== GETGAME() METHOD TESTS ==========

    @Test
    void getGame_Success_ShouldReturnGameState() {
        // Given
        String gameId = mockGameState.getGameId();
        when(gameService.getGame(gameId)).thenReturn(mockGameState);

        // When
        ResponseEntity<GameState> response = gameController.getGame(gameId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(gameId, response.getBody().getGameId());
        assertEquals("WAITING", response.getBody().getStatus());
        verify(gameService, times(1)).getGame(gameId);
    }

    @Test
    void getGame_NotFound_ShouldReturnNotFound() {
        // Given
        String gameId = "non-existent-game-id";
        when(gameService.getGame(gameId)).thenReturn(null);

        // When
        ResponseEntity<GameState> response = gameController.getGame(gameId);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(gameService, times(1)).getGame(gameId);
    }

    @Test
    void getGame_Exception_ShouldReturnInternalServerError() {
        // Given
        String gameId = "test-game-id-exception";
        when(gameService.getGame(gameId)).thenThrow(new RuntimeException("Some Error"));

        // When
        ResponseEntity<GameState> response = gameController.getGame(gameId);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(gameService, times(1)).getGame(gameId);
    }
}