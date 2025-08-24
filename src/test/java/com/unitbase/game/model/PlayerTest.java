package com.unitbase.game.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Map;

class PlayerTest {

    private Player player;
    private static final String PLAYER_NAME = "TestPlayer";
    private static final String PLAYER_COLOR = "RED";

    @BeforeEach
    void setUp() {
        player = new Player(PLAYER_NAME, PLAYER_COLOR);
    }

    // ========== CONSTRUCTOR TESTS ==========

    @Test
    @DisplayName("Constructor should initialize player with correct name and color")
    void constructor_ShouldInitializePlayerWithCorrectNameAndColor() {
        // When
        Player newPlayer = new Player("John", "BLUE");

        // Then
        assertEquals("John", newPlayer.getName());
        assertEquals("BLUE", newPlayer.getColor());
    }

    @Test
    @DisplayName("Constructor should initialize rings with 3 of each size")
    void constructor_ShouldInitializeRingsCorrectly() {
        // Then
        Map<String, Integer> rings = player.getRings();
        assertEquals(3, rings.get("SMALL"));
        assertEquals(3, rings.get("MEDIUM"));
        assertEquals(3, rings.get("LARGE"));
        assertEquals(3, rings.size()); // Should only have these 3 entries
    }


    // ========== HASRING TESTS ==========

    @Test
    @DisplayName("hasRing should return true for valid ring sizes with available rings")
    void hasRing_ValidSizeWithAvailableRings_ShouldReturnTrue() {
        // Then
        assertTrue(player.hasRing("SMALL"));
        assertTrue(player.hasRing("MEDIUM"));
        assertTrue(player.hasRing("LARGE"));
    }

    @Test
    @DisplayName("hasRing should return false when no rings of specified size remain")
    void hasRing_NoRingsRemaining_ShouldReturnFalse() {
        // Given - Use all small rings
        player.useRing("SMALL");
        player.useRing("SMALL");
        player.useRing("SMALL");

        // When & Then
        assertFalse(player.hasRing("SMALL"));
        assertTrue(player.hasRing("MEDIUM")); // Others should still be available
        assertTrue(player.hasRing("LARGE"));
    }

    @Test
    @DisplayName("hasRing should return false for invalid ring size")
    void hasRing_InvalidSize_ShouldReturnFalse() {
        // When & Then
        assertFalse(player.hasRing("EXTRA_LARGE"));
        assertFalse(player.hasRing("TINY"));
        assertFalse(player.hasRing("INVALID"));
        assertFalse(player.hasRing(""));
    }

    // ========== HASANYRINGS TESTS ==========

    @Test
    @DisplayName("hasAnyRings should return true when player has rings")
    void hasAnyRings_PlayerHasRings_ShouldReturnTrue() {
        // When & Then
        assertTrue(player.hasAnyRings());
    }

    @Test
    @DisplayName("hasAnyRings should return true when player has only one ring remaining")
    void hasAnyRings_OneRingRemaining_ShouldReturnTrue() {
        // Given - Use all but one ring
        player.useRing("SMALL");
        player.useRing("SMALL");
        player.useRing("SMALL"); // No small rings left
        player.useRing("MEDIUM");
        player.useRing("MEDIUM");
        player.useRing("MEDIUM"); // No medium rings left
        player.useRing("LARGE");
        player.useRing("LARGE"); // One large ring left

        // When & Then
        assertTrue(player.hasAnyRings());
        assertTrue(player.hasRing("LARGE"));
        assertFalse(player.hasRing("SMALL"));
        assertFalse(player.hasRing("MEDIUM"));
    }

    @Test
    @DisplayName("hasAnyRings should return false when player has no rings")
    void hasAnyRings_NoRingsRemaining_ShouldReturnFalse() {
        // Given - Use all rings
        for (int i = 0; i < 3; i++) {
            player.useRing("SMALL");
            player.useRing("MEDIUM");
            player.useRing("LARGE");
        }

        // When & Then
        assertFalse(player.hasAnyRings());
        assertFalse(player.hasRing("SMALL"));
        assertFalse(player.hasRing("MEDIUM"));
        assertFalse(player.hasRing("LARGE"));
    }

    // ========== USE RING TESTS ==========

    @Test
    @DisplayName("useRing should decrease ring count for valid ring size")
    void useRing_ValidSize_ShouldDecreaseCount() {
        // Given
        int initialSmallCount = player.getRings().get("SMALL");

        // When
        player.useRing("SMALL");

        // Then
        assertEquals(initialSmallCount - 1, player.getRings().get("SMALL"));
        assertEquals(3, player.getRings().get("MEDIUM")); // Others unchanged
        assertEquals(3, player.getRings().get("LARGE"));
    }

    @Test
    @DisplayName("useRing should not decrease count below zero")
    void useRing_NoRingsAvailable_ShouldNotGoNegative() {
        // Given - Use all small rings first
        player.useRing("SMALL");
        player.useRing("SMALL");
        player.useRing("SMALL");
        assertEquals(0, player.getRings().get("SMALL"));

        // When - Try to use another small ring
        player.useRing("SMALL");

        // Then - Should still be 0, not negative
        assertEquals(0, player.getRings().get("SMALL"));
    }

    @Test
    @DisplayName("useRing should handle multiple uses of same ring size")
    void useRing_MultipleUses_ShouldDecreaseProperly() {
        // When
        player.useRing("MEDIUM");
        player.useRing("MEDIUM");

        // Then
        assertEquals(1, player.getRings().get("MEDIUM"));
        assertEquals(3, player.getRings().get("SMALL")); // Others unchanged
        assertEquals(3, player.getRings().get("LARGE"));
    }

    @Test
    @DisplayName("useRing should handle case sensitivity")
    void useRing_CaseSensitive_ShouldNotAffectRings() {
        // Given
        int initialSmallCount = player.getRings().get("SMALL");

        // When
        player.useRing("small"); // lowercase
        player.useRing("Small"); // mixed case

        // Then - Should not affect actual SMALL rings
        assertEquals(initialSmallCount, player.getRings().get("SMALL"));
    }

    // ========== GETTER TESTS ==========

    @Test
    @DisplayName("getName should return correct name")
    void getName_ShouldReturnCorrectName() {
        // When & Then
        assertEquals(PLAYER_NAME, player.getName());
    }

    @Test
    @DisplayName("getColor should return correct color")
    void getColor_ShouldReturnCorrectColor() {
        // When & Then
        assertEquals(PLAYER_COLOR, player.getColor());
    }

    @Test
    @DisplayName("getRings should return mutable map")
    void getRings_ShouldReturnMutableMap() {
        // When
        Map<String, Integer> rings = player.getRings();

        // Then
        assertNotNull(rings);
        assertEquals(3, rings.size());
        assertTrue(rings.containsKey("SMALL"));
        assertTrue(rings.containsKey("MEDIUM"));
        assertTrue(rings.containsKey("LARGE"));
    }

    // ========== INTEGRATION TESTS ==========

    @Test
    @DisplayName("Complete ring usage scenario should work correctly")
    void integrationTest_CompleteRingUsage_ShouldWorkCorrectly() {
        // Given - Fresh player
        assertTrue(player.hasAnyRings());
        assertTrue(player.hasRing("SMALL"));
        assertTrue(player.hasRing("MEDIUM"));
        assertTrue(player.hasRing("LARGE"));

        // When - Use some rings
        player.useRing("SMALL");
        player.useRing("SMALL");
        player.useRing("MEDIUM");

        // Then
        assertTrue(player.hasRing("SMALL")); // 1 left
        assertTrue(player.hasRing("MEDIUM")); // 2 left
        assertTrue(player.hasRing("LARGE")); // 3 left
        assertTrue(player.hasAnyRings());

        // When - Use remaining small ring
        player.useRing("SMALL");

        // Then
        assertFalse(player.hasRing("SMALL")); // 0 left
        assertTrue(player.hasRing("MEDIUM")); // 2 left
        assertTrue(player.hasRing("LARGE")); // 3 left
        assertTrue(player.hasAnyRings());

        // When - Use all remaining rings
        player.useRing("MEDIUM");
        player.useRing("MEDIUM");
        player.useRing("LARGE");
        player.useRing("LARGE");
        player.useRing("LARGE");

        // Then
        assertFalse(player.hasAnyRings());
        assertFalse(player.hasRing("SMALL"));
        assertFalse(player.hasRing("MEDIUM"));
        assertFalse(player.hasRing("LARGE"));
    }

}