package com.unitbase.game.model;

import java.util.HashMap;
import java.util.Map;

public class Player {
    private String name;
    private String color;
    private Map<String, Integer> rings;

    public Player(String name, String color) {
        this.name = name;
        this.color = color;
        this.rings = new HashMap<>();
        rings.put("SMALL", 3);
        rings.put("MEDIUM", 3);
        rings.put("LARGE", 3);
    }

    public boolean hasRing(String size) {
        return rings.getOrDefault(size, 0) > 0;
    }

    public boolean hasAnyRings() {
        return rings.values().stream().anyMatch(count -> count > 0);
    }

    public void useRing(String size) {
        if (hasRing(size)) {
            rings.put(size, rings.get(size) - 1);
        }
    }

    // Getters
    public String getName() { return name; }
    public String getColor() { return color; }
    public Map<String, Integer> getRings() { return rings; }
}
