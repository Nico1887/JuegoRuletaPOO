package models;

import models.Pocket;
import java.util.List;

/**
 * Represents the spinning part of the roulette.
 * Provides access to the pockets managed by the Table.
 */
public class Wheel {
    // ▶ Attributes ─────────────────────────────────────────────────────────────────────────────────────────────
    private final Table table;

    // ▶ Constructors ───────────────────────────────────────────────────────────────────────────────────────────
    public Wheel(Table table) {
        this.table = table;
    }

    // ▶ Getters ────────────────────────────────────────────────────────────────────────────────────────────────
    public List<Pocket> getPockets() {
        return table.getPockets();
    }
}