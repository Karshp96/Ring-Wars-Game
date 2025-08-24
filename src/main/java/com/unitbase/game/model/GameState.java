package com.unitbase.game.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GameState {
    public static final int BOARD_SIZE = 3;
    public static final String[] COLORS = {"RED", "BLUE", "GREEN", "YELLOW"};
    public static final String[] SIZES = {"SMALL", "MEDIUM", "LARGE"};

    private final String gameId;
    private final List<Player> players;
    private int currentPlayerIndex;
    private final Cell[][] board;
    private String status; // "WAITING", "PLAYING", "FINISHED"
    private String winner;
    private List<String> winningLine;
    private long lastActivity;

    public GameState() {
        this.gameId = UUID.randomUUID().toString();
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.board = new Cell[BOARD_SIZE][BOARD_SIZE];
        this.status = "WAITING";
        this.lastActivity = System.currentTimeMillis();
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = new Cell();
            }
        }
    }

    public boolean addPlayer(String playerName) {
        if (players.size() >= 2 || status.equals("PLAYING")) {
            return false;
        }

        String color = COLORS[players.size()];
        Player player = new Player(playerName, color);
        players.add(player);

        if (players.size() == 2) {
            status = "PLAYING";
        }

        lastActivity = System.currentTimeMillis();
        return true;
    }

    public boolean makeMove(int row, int col, String size, String playerColor) {
        if (!status.equals("PLAYING") || row < 0 || row >= BOARD_SIZE ||
                col < 0 || col >= BOARD_SIZE) {
            return false;
        }

        Player currentPlayer = players.get(currentPlayerIndex);
        if (!currentPlayer.getColor().equals(playerColor)) {
            return false;
        }

        if (!currentPlayer.hasRing(size)) {
            return false;
        }

        Cell cell = board[row][col];
        if (!cell.canPlaceRing(size, playerColor)) {
            return false;
        }

        cell.placeRing(size, playerColor);
        currentPlayer.useRing(size);

        // Check for win
        String winResult = checkWin();
        if (winResult != null) {
            winner = winResult;
            status = "FINISHED";
        } else {
            nextPlayer();
        }

        lastActivity = System.currentTimeMillis();
        return true;
    }

    private void nextPlayer() {
        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } while (!players.get(currentPlayerIndex).hasAnyRings() && hasPlayableRings());
    }

    private boolean hasPlayableRings() {
        return players.stream().anyMatch(Player::hasAnyRings);
    }

    public String checkWin() {
        // Check Concentric rings (same cell) first
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Cell cell = board[i][j];
                for (String color : COLORS) {
                    if (cell.hasRing("SMALL", color) &&
                            cell.hasRing("MEDIUM", color) &&
                            cell.hasRing("LARGE", color)) {
                        winningLine = List.of(i + "," + j);
                        return color;
                    }
                }
            }
        }

        // Check all possible lines for wins
        List<List<int[]>> lines = getAllLines();

        for (List<int[]> line : lines) {
            String winner = checkLineWin(line);
            if (winner != null) {
                winningLine = new ArrayList<>();
                for (int[] pos : line) {
                    winningLine.add(pos[0] + "," + pos[1]);
                }
                return winner;
            }
        }
        return null;
    }

    private String checkLineWin(List<int[]> line) {
        // Win condition 1: Same size rings
        for (String size : SIZES) {
            for (String color : COLORS) {
                if (line.stream().allMatch(pos -> {
                    Cell cell = board[pos[0]][pos[1]];
                    Ring topRing = cell.getTopRingOfSize(size);
                    return topRing != null && topRing.getColor().equals(color);
                })) {
                    return color;
                }
            }
        }

        // Win condition 2: Ascending/Descending size order
        for (String color : COLORS) {
            if (checkSizeOrder(line, color, true) || checkSizeOrder(line, color, false)) {
                return color;
            }
        }

        return null;
    }

    private boolean checkSizeOrder(List<int[]> line, String color, boolean ascending) {
        String[] order = ascending ?
                new String[]{"SMALL", "MEDIUM", "LARGE"} :
                new String[]{"LARGE", "MEDIUM", "SMALL"};

        for (int i = 0; i < line.size(); i++) {
            int[] pos = line.get(i);
            Cell cell = board[pos[0]][pos[1]];
            Ring topRing = cell.getTopRingOfSize(order[i]);
            if (topRing == null || !topRing.getColor().equals(color)) {
                return false;
            }
        }
        return true;
    }

    private List<List<int[]>> getAllLines() {
        List<List<int[]>> lines = new ArrayList<>();

        // Rows
        for (int i = 0; i < BOARD_SIZE; i++) {
            List<int[]> row = new ArrayList<>();
            for (int j = 0; j < BOARD_SIZE; j++) {
                row.add(new int[]{i, j});
            }
            lines.add(row);
        }

        // Columns
        for (int j = 0; j < BOARD_SIZE; j++) {
            List<int[]> col = new ArrayList<>();
            for (int i = 0; i < BOARD_SIZE; i++) {
                col.add(new int[]{i, j});
            }
            lines.add(col);
        }

        // Diagonals
        List<int[]> diag1 = Arrays.asList(new int[]{0,0}, new int[]{1,1}, new int[]{2,2});
        List<int[]> diag2 = Arrays.asList(new int[]{0,2}, new int[]{1,1}, new int[]{2,0});
        lines.add(diag1);
        lines.add(diag2);

        return lines;
    }

    // Getters and setters
    public String getGameId() { return gameId; }
    public List<Player> getPlayers() { return players; }
    public int getCurrentPlayerIndex() { return currentPlayerIndex; }
    public Cell[][] getBoard() { return board; }
    public String getStatus() { return status; }
    public String getWinner() { return winner; }
    public List<String> getWinningLine() { return winningLine; }
    public long getLastActivity() { return lastActivity; }

    public Player getCurrentPlayer() {
        if (players.isEmpty()) return null;
        return players.get(currentPlayerIndex);
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void setWinningLine(List<String> winningLine) {
        this.winningLine = winningLine;
    }

    public void setLastActivity(long lastActivity) {
        this.lastActivity = lastActivity;
    }
}
