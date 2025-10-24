package models;

// Roulette wheel for Rainbow Roulette
// Contains 50 pockets and generates random spin results

// ▶ Imports ────────────────────────────────────────────────────────────────────────────────────────────────────
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import enums.Color;

public class Wheel {
    // ▶ Attributes ─────────────────────────────────────────────────────────────────────────────────────────────
    private final Pocket[] pockets;
    private final Random random;

    // ▶ Constructors ───────────────────────────────────────────────────────────────────────────────────────────
    public Wheel() {
        this.pockets = new Pocket[50];
        this.random = new Random();

        List<Pocket> temporaryPocketList = new ArrayList<>(50);

        Color[] standardColors = {
                Color.RED, Color.ORANGE, Color.YELLOW,
                Color.GREEN, Color.BLUE, Color.VIOLET
        };

        // 1. Add 48 pockets for the standard colors (8 of each).
        for (Color color : standardColors) {
            for (int i = 0; i < 8; i++) {
                temporaryPocketList.add(new Pocket(0, color));
            }
        }

        // 2. Add the 2 special BLACK pockets.
        temporaryPocketList.add(new Pocket(0, Color.BLACK));
        temporaryPocketList.add(new Pocket(0, Color.BLACK));

        // 3. Shuffle the temporary list of pockets.
        // This ensures the colors are distributed in an interleaved/random pattern,
        // mimicking a real roulette wheel layout for fairness.
        Collections.shuffle(temporaryPocketList, this.random);

        // 4. Populate the final 'pockets' array with numbered pockets from the shuffled list.
        for (int i = 0; i < 50; i++) {
            int number = i + 1;
            Pocket shuffledPocket = temporaryPocketList.get(i);
            this.pockets[i] = new Pocket(number, shuffledPocket.getColor());
        }
    }

    // ▶ Getters ────────────────────────────────────────────────────────────────────────────────────────────────
    public Pocket[] getPockets() {
        return pockets;
    }

    // ▶ Methods ────────────────────────────────────────────────────────────────────────────────────────────────
    /**
     * Simulates the spinning of the roulette wheel and the landing of two balls.
     */
    public Pocket[] spin(){
        Pocket first = pockets[random.nextInt(pockets.length)];
        Pocket second = pockets[random.nextInt(pockets.length)];

        return new Pocket[] {first, second};
    }

}