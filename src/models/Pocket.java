package models;

// Single slot on the roulette wheel.
// Has a unique number and color.

// ▶ Imports ────────────────────────────────────────────────────────────────────────────────────────────────────
import enums.stakes.ColorStake;

public class Pocket {

    // ▶ Attributes ──────────────────────────────────────────────────────────────────────────────────────────────
    private final int number;
    private final ColorStake color;

    // ▶ Constructors ────────────────────────────────────────────────────────────────────────────────────────────
    public Pocket(int number, ColorStake color) {
        this.number = number;
        this.color = color;
    }

    // ▶ Getters ─────────────────────────────────────────────────────────────────────────────────────────────────
    public int getNumber() {
        return number;
    }
    public ColorStake getColor() {
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
