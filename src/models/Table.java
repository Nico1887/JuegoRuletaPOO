package models;

import enums.stakes.ColorStake;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the physical structure of the 50-pocket roulette wheel layout.
 * Manages the pocket definitions.
 */
public class Table {
    // ▶ Attributes ─────────────────────────────────────────────────────────────────────────────────────────────
    private final List<Pocket> pockets;

    // ▶ Constructors ───────────────────────────────────────────────────────────────────────────────────────────
    public Table() {
        this.pockets = new ArrayList<>();
        initializePockets();
    }

    // ▶ Getters ────────────────────────────────────────────────────────────────────────────────────────────────
    public List<Pocket> getPockets() {
        return pockets;
    }

    // ▶ Methods ────────────────────────────────────────────────────────────────────────────────────────────────
    /**
     * Initializes the 50-pocket board with the "Rainbow" color distribution,
     * assigning colors based on the sequential index of non-special numbers.
     */
    private void initializePockets() {
        this.pockets.clear();

        // 6 Main Colors used for the repeating sequence
        ColorStake[] mainColors = {
                ColorStake.RED, ColorStake.ORANGE, ColorStake.YELLOW,
                ColorStake.GREEN, ColorStake.BLUE, ColorStake.VIOLET
        };

        // Special numbers (BLACK)
        List<Integer> specialNumbers = Arrays.asList(25, 50);
        List<Integer> regularNumbers = new ArrayList<>();

        for (int i = 1; i <= 50; i++) {
            if (!specialNumbers.contains(i)) {
                regularNumbers.add(i);
            }
        }

        // --- Assign Colors to Numbers ---
        Map<Integer, ColorStake> finalAssignment = new HashMap<>();

        // 1. Assign Special Colors (BLACK)
        finalAssignment.put(25, ColorStake.BLACK);
        finalAssignment.put(50, ColorStake.BLACK);

        // 2. Assign Main Colors sequentially
        int colorIndex = 0;
        int colorsCount = mainColors.length;

        for (int num : regularNumbers) {
            finalAssignment.put(num, mainColors[colorIndex % colorsCount]);
            colorIndex++;
        }

        // 3. Create the Pocket List in numerical order (1 to 50)
        for (int i = 1; i <= 50; i++) {
            this.pockets.add(new Pocket(i, finalAssignment.get(i)));
        }
    }
}