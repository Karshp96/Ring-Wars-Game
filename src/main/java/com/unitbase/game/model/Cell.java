package com.unitbase.game.model;

import java.util.Stack;

public class Cell {
    private Stack<Ring> rings;

    public Cell() {
        this.rings = new Stack<>();
    }

    public boolean canPlaceRing(String size, String color) {
        // Check if there's already a ring of the same size (regardless of color)
        return rings.stream().noneMatch(ring -> ring.getSize().equals(size));
    }

    public void placeRing(String size, String color) {
        rings.push(new Ring(size, color));
    }

    public boolean hasRing(String size, String color) {
        return rings.stream().anyMatch(ring ->
                ring.getSize().equals(size) && ring.getColor().equals(color));
    }

    public boolean hasTopRing(String size, String color) {
        if (rings.isEmpty()) return false;
        Ring topRing = rings.peek();
        return topRing.getSize().equals(size) && topRing.getColor().equals(color);
    }

    public Ring getTopRingOfSize(String size) {
        // Find the most recently placed ring of the given size
        for (int i = rings.size() - 1; i >= 0; i--) {
            Ring ring = rings.get(i);
            if (ring.getSize().equals(size)) {
                return ring;
            }
        }
        return null;
    }

    private int getSizeValue(String size) {
        switch (size) {
            case "LARGE": return 3;
            case "MEDIUM": return 2;
            case "SMALL": return 1;
            default: return 0;
        }
    }

    public Stack<Ring> getRings() { return rings; }
}