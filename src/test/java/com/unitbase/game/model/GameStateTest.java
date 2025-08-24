package com.unitbase.game.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    private GameState gameState;

    @BeforeEach
    void setUp() {
        gameState = new GameState();
    }

    // ========== INITIALIZATION TESTS ==========

    @Test
    @DisplayName("Constructor should initialize game state correctly")
    void constructor_ShouldInitializeGameStateCorrectly() {
        // Then
        assertNotNull(gameState.getGameId());
        assertTrue(gameState.getPlayers().isEmpty());
        assertEquals(0, gameState.getCurrentPlayerIndex());
        assertNotNull(gameState.getBoard());
        assertEquals(GameState.BOARD_SIZE, gameState.getBoard().length);
        assertEquals(GameState.BOARD_SIZE, gameState.getBoard()[0].length);
        assertEquals("WAITING", gameState.getStatus());
        assertNull(gameState.getWinner());
        assertNull(gameState.getWinningLine());
        assertTrue(gameState.getLastActivity() > 0);
    }

    @Test
    @DisplayName("Board should be initialized with empty cells")
    void boardInitialization_ShouldCreateEmptyCells() {
        // Then
        Cell[][] board = gameState.getBoard();
        for (int i = 0; i < GameState.BOARD_SIZE; i++) {
            for (int j = 0; j < GameState.BOARD_SIZE; j++) {
                assertNotNull(board[i][j]);
            }
        }
    }

    // ========== ADD PLAYER TESTS ==========

    @Test
    @DisplayName("Should successfully add first player")
    void addPlayer_FirstPlayer_ShouldSucceed() {
        // When
        boolean result = gameState.addPlayer("Player1");

        // Then
        assertTrue(result);
        assertEquals(1, gameState.getPlayers().size());
        assertEquals("Player1", gameState.getPlayers().getFirst().getName());
        assertEquals("RED", gameState.getPlayers().getFirst().getColor());
        assertEquals("WAITING", gameState.getStatus());
    }

    @Test
    @DisplayName("Should successfully add second player and start game")
    void addPlayer_SecondPlayer_ShouldStartGame() {
        // Given
        gameState.addPlayer("Player1");

        // When
        boolean result = gameState.addPlayer("Player2");

        // Then
        assertTrue(result);
        assertEquals(2, gameState.getPlayers().size());
        assertEquals("Player2", gameState.getPlayers().get(1).getName());
        assertEquals("BLUE", gameState.getPlayers().get(1).getColor());
        assertEquals("PLAYING", gameState.getStatus());
    }

    @Test
    @DisplayName("Should reject third player")
    void addPlayer_ThirdPlayer_ShouldFail() {
        // Given
        gameState.addPlayer("Player1");
        gameState.addPlayer("Player2");

        // When
        boolean result = gameState.addPlayer("Player3");

        // Then
        assertFalse(result);
        assertEquals(2, gameState.getPlayers().size());
    }

    @Test
    @DisplayName("Should reject player when game is already playing")
    void addPlayer_GameAlreadyPlaying_ShouldFail() {
        // Given
        gameState.addPlayer("Player1");
        gameState.addPlayer("Player2");
        gameState.setStatus("PLAYING");

        // When
        boolean result = gameState.addPlayer("Player3");

        // Then
        assertFalse(result);
        assertEquals(2, gameState.getPlayers().size());
    }

    // ========== MAKE MOVE TESTS ==========

    @Test
    @DisplayName("Should reject move when game is not playing")
    void makeMove_GameNotPlaying_ShouldFail() {
        // Given
        gameState.addPlayer("Player1");
        // Game status is still "WAITING"

        // When
        boolean result = gameState.makeMove(0, 0, "SMALL", "RED");

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("Should reject move with invalid coordinates")
    void makeMove_InvalidCoordinates_ShouldFail() {
        // Given
        gameState.addPlayer("Player1");
        gameState.addPlayer("Player2");

        // When & Then
        assertFalse(gameState.makeMove(-1, 0, "SMALL", "RED"));
        assertFalse(gameState.makeMove(0, -1, "SMALL", "RED"));
        assertFalse(gameState.makeMove(3, 0, "SMALL", "RED"));
        assertFalse(gameState.makeMove(0, 3, "SMALL", "RED"));
    }

    @Test
    @DisplayName("Should reject move from wrong player")
    void makeMove_WrongPlayer_ShouldFail() {
        // Given
        gameState.addPlayer("Player1");
        gameState.addPlayer("Player2");
        // Current player is index 0 (RED)

        // When
        boolean result = gameState.makeMove(0, 0, "SMALL", "BLUE");

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("Should successfully make valid move and switch players")
    void makeMove_ValidMove_ShouldSucceedAndSwitchPlayers() {
        // Given
        gameState.addPlayer("Player1");
        gameState.addPlayer("Player2");
        int initialPlayerIndex = gameState.getCurrentPlayerIndex();

        // When
        boolean result = gameState.makeMove(0, 0, "SMALL", "RED");

        // Then
        assertTrue(result);
        assertNotEquals(initialPlayerIndex, gameState.getCurrentPlayerIndex());
    }

    // ========== WIN CONDITION TESTS ==========

    @Test
    @DisplayName("Should detect horizontal line win with same size rings")
    void checkWin_HorizontalLineSameSize_ShouldDetectWin() {
        // Given
        gameState.addPlayer("Player1");
        gameState.addPlayer("Player2");

        // When - Place three SMALL RED rings horizontally
        gameState.makeMove(0, 0, "SMALL", "RED");
        gameState.makeMove(1, 0, "SMALL", "BLUE"); // Blue's turn
        gameState.makeMove(0, 1, "SMALL", "RED");
        gameState.makeMove(1, 1, "SMALL", "BLUE"); // Blue's turn
        gameState.makeMove(0, 2, "SMALL", "RED");

        // Then
        assertEquals("FINISHED", gameState.getStatus());
        assertEquals("RED", gameState.getWinner());
        assertNotNull(gameState.getWinningLine());
        assertEquals(3, gameState.getWinningLine().size());
    }

    @Test
    @DisplayName("Should detect vertical line win with same size rings")
    void checkWin_VerticalLineSameSize_ShouldDetectWin() {
        // Given
        gameState.addPlayer("Player1");
        gameState.addPlayer("Player2");

        // When - Place three MEDIUM RED rings vertically
        gameState.makeMove(0, 0, "MEDIUM", "RED");
        gameState.makeMove(0, 1, "MEDIUM", "BLUE");
        gameState.makeMove(1, 0, "MEDIUM", "RED");
        gameState.makeMove(0, 2, "MEDIUM", "BLUE");
        gameState.makeMove(2, 0, "MEDIUM", "RED");

        // Then
        assertEquals("FINISHED", gameState.getStatus());
        assertEquals("RED", gameState.getWinner());
        assertNotNull(gameState.getWinningLine());
        assertEquals(3, gameState.getWinningLine().size());
    }

    @Test
    @DisplayName("Should detect diagonal line win with same size rings")
    void checkWin_DiagonalLineSameSize_ShouldDetectWin() {
        // Given
        gameState.addPlayer("Player1");
        gameState.addPlayer("Player2");

        // When - Place three LARGE BLUE rings diagonally
        gameState.makeMove(0, 0, "SMALL", "RED"); // RED's turn
        gameState.makeMove(0, 0, "LARGE", "BLUE"); // BLUE places LARGE on top
        gameState.makeMove(0, 1, "SMALL", "RED");
        gameState.makeMove(1, 1, "LARGE", "BLUE");
        gameState.makeMove(0, 2, "SMALL", "RED");

        // Then
        assertEquals("FINISHED", gameState.getStatus());
        assertEquals("RED", gameState.getWinner());
        assertNotNull(gameState.getWinningLine());
        assertEquals(3, gameState.getWinningLine().size());
    }

    @Test
    @DisplayName("Should detect concentric rings win")
    void checkWin_ConcentricRings_ShouldDetectWin() {
        // Given
        gameState.addPlayer("Player1");
        gameState.addPlayer("Player2");

        // When - Place all three sizes of RED rings in same cell
        gameState.makeMove(1, 1, "LARGE", "RED");
        gameState.makeMove(0, 0, "SMALL", "BLUE");
        gameState.makeMove(1, 1, "MEDIUM", "RED");
        gameState.makeMove(0, 1, "SMALL", "BLUE");
        gameState.makeMove(1, 1, "SMALL", "RED");

        // Then
        assertEquals("FINISHED", gameState.getStatus());
        assertEquals("RED", gameState.getWinner());
        assertNotNull(gameState.getWinningLine());
        assertEquals(1, gameState.getWinningLine().size());
        assertEquals("1,1", gameState.getWinningLine().getFirst());
    }

    @Test
    @DisplayName("Should detect ascending size order win")
    void checkWin_AscendingSizeOrder_ShouldDetectWin() {
        // Given
        gameState.addPlayer("Player1");
        gameState.addPlayer("Player2");

        // When - Place RED rings in ascending size order horizontally (SMALL, MEDIUM, LARGE)
        gameState.makeMove(0, 0, "SMALL", "RED");
        gameState.makeMove(1, 0, "SMALL", "BLUE");
        gameState.makeMove(0, 1, "MEDIUM", "RED");
        gameState.makeMove(1, 1, "SMALL", "BLUE");
        gameState.makeMove(0, 2, "LARGE", "RED");

        // Then
        assertEquals("FINISHED", gameState.getStatus());
        assertEquals("RED", gameState.getWinner());
        assertNotNull(gameState.getWinningLine());
        assertEquals(3, gameState.getWinningLine().size());
    }

    @Test
    @DisplayName("Should detect descending size order win")
    void checkWin_DescendingSizeOrder_ShouldDetectWin() {
        // Given
        gameState.addPlayer("Player1");
        gameState.addPlayer("Player2");

        // When - Place BLUE rings in descending size order vertically (LARGE, MEDIUM, SMALL)
        gameState.makeMove(0, 0, "SMALL", "RED");
        gameState.makeMove(0, 1, "LARGE", "BLUE");
        gameState.makeMove(1, 0, "SMALL", "RED");
        gameState.makeMove(1, 1, "MEDIUM", "BLUE");
        gameState.makeMove(2, 0, "SMALL", "RED");

        // Then
        assertEquals("FINISHED", gameState.getStatus());
        assertEquals("RED", gameState.getWinner());
        assertNotNull(gameState.getWinningLine());
        assertEquals(3, gameState.getWinningLine().size());
    }

    @Test
    @DisplayName("Should not detect win when no winning condition is met")
    void checkWin_NoWinningCondition_ShouldReturnNull() {
        // Given
        gameState.addPlayer("Player1");
        gameState.addPlayer("Player2");

        // When - Make some moves that don't result in a win
        gameState.makeMove(0, 0, "SMALL", "RED");
        gameState.makeMove(0, 1, "SMALL", "BLUE");
        gameState.makeMove(1, 0, "MEDIUM", "RED");

        // Then
        assertEquals("PLAYING", gameState.getStatus());
        assertNull(gameState.getWinner());
        assertNull(gameState.getWinningLine());
    }

    // ========== GETTER TESTS ==========

    @Test
    @DisplayName("getCurrentPlayer should return correct current player")
    void getCurrentPlayer_ShouldReturnCorrectPlayer() {
        // Given
        gameState.addPlayer("Player1");
        gameState.addPlayer("Player2");

        // When
        Player currentPlayer = gameState.getCurrentPlayer();

        // Then
        assertNotNull(currentPlayer);
        assertEquals("Player1", currentPlayer.getName());
        assertEquals("RED", currentPlayer.getColor());
    }

    @Test
    @DisplayName("getCurrentPlayer should return null when no players")
    void getCurrentPlayer_NoPlayers_ShouldReturnNull() {
        // When
        Player currentPlayer = gameState.getCurrentPlayer();

        // Then
        assertNull(currentPlayer);
    }

    // ========== SETTER TESTS ==========

    @Test
    @DisplayName("Setters should update game state correctly")
    void setters_ShouldUpdateGameStateCorrectly() {
        // When
        gameState.setCurrentPlayerIndex(1);
        gameState.setStatus("FINISHED");
        gameState.setWinner("BLUE");
        gameState.setWinningLine(Arrays.asList("0,0", "0,1", "0,2"));
        gameState.setLastActivity(12345L);

        // Then
        assertEquals(1, gameState.getCurrentPlayerIndex());
        assertEquals("FINISHED", gameState.getStatus());
        assertEquals("BLUE", gameState.getWinner());
        assertEquals(Arrays.asList("0,0", "0,1", "0,2"), gameState.getWinningLine());
        assertEquals(12345L, gameState.getLastActivity());
    }

    // ========== ACTIVITY TRACKING TESTS ==========

    @Test
    @DisplayName("Should update last activity when adding player")
    void addPlayer_ShouldUpdateLastActivity() {
        // Given
        long initialActivity = gameState.getLastActivity();

        // When
        try {
            Thread.sleep(1); // Ensure time difference
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        gameState.addPlayer("Player1");

        // Then
        assertTrue(gameState.getLastActivity() > initialActivity);
    }

    @Test
    @DisplayName("Should update last activity when making move")
    void makeMove_ShouldUpdateLastActivity() {
        // Given
        gameState.addPlayer("Player1");
        gameState.addPlayer("Player2");
        long initialActivity = gameState.getLastActivity();

        // When
        try {
            Thread.sleep(1); // Ensure time difference
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        gameState.makeMove(0, 0, "SMALL", "RED");

        // Then
        assertTrue(gameState.getLastActivity() > initialActivity);
    }

    // ========== CONSTANTS TESTS ==========

    @Test
    @DisplayName("Constants should have expected values")
    void constants_ShouldHaveExpectedValues() {
        // Then
        assertEquals(3, GameState.BOARD_SIZE);
        assertArrayEquals(new String[]{"RED", "BLUE", "GREEN", "YELLOW"}, GameState.COLORS);
        assertArrayEquals(new String[]{"SMALL", "MEDIUM", "LARGE"}, GameState.SIZES);
    }
}