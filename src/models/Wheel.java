package models;

// Roulette wheel for Rainbow Roulette
// Contains 50 pockets and generates random spin results

// ▶ Imports ────────────────────────────────────────────────────────────────────────────────────────────────────
import java.util.Random;

public class Wheel {
    // ▶ Attributes ─────────────────────────────────────────────────────────────────────────────────────────────
    private final Pocket[] pockets;   // Array representing the 50 pockets of the roulette wheel.
    private final Random random;      // Generator used to simulate spins.

    // ▶ Constructors ───────────────────────────────────────────────────────────────────────────────────────────
    public Wheel() {
        this.pockets = new Pocket[50];
        this.random = new Random();     // Creates a new random generator instance

        /*
        Definir la lógica de distribución de los colores:
        for (int slot = 0; slot < pockets.length; slot ++) {
            pockets[slot] = new Pocket(1,Color.RED); // TODO generalizar //TODO agregar excepcion de rango 1-50.
        }
         */

    }

    // ▶ Getters ────────────────────────────────────────────────────────────────────────────────────────────────
    public Pocket[] getPockets() {
        return pockets;
    }

    // ▶ Methods ────────────────────────────────────────────────────────────────────────────────────────────────
    public Pocket[] spin(){
        Pocket first = pockets[random.nextInt(pockets.length)];
        Pocket second = pockets[random.nextInt(pockets.length)];

        return new Pocket[] {first, second};
    }

}
