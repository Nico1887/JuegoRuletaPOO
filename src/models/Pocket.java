package models;

// Single slot on the roulette wheel
// Has a unique number and color

// ▶ Imports ────────────────────────────────────────────────────────────────────────────────────────────────────
import enums.Color;

public class Pocket {

    // ▶ Attributes ──────────────────────────────────────────────────────────────────────────────────────────────
    private final int number;
    private final Color color;

    // ▶ Constructors ────────────────────────────────────────────────────────────────────────────────────────────
    public Pocket(int number, Color color) {
        this.number = number;
        this.color = color;
    }

    // ▶ Getters ─────────────────────────────────────────────────────────────────────────────────────────────────
    public int getNumber() {
        return number;
    }
    public Color getColor() {
        return color;
    }

    // ▶ ToString ────────────────────────────────────────────────────────────────────────────────────────────────
    public String toString() {
        return "Pocket{" +
                "number=" + number +
                ", color=" + color +
                '}';
    }

}
