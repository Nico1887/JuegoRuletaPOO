package models;

// Player in Rainbow Roulette
// Has balance, can place bets, and receives winnings/losses

// ▶ Imports ────────────────────────────────────────────────────────────────────────────────────────────────────
import java.util.ArrayList;
import java.util.List;

public class Player {
    // ▶ Attributes ─────────────────────────────────────────────────────────────────────────────────────────────
    private final String username;
    private double balance;         // Money
    private List<Bet> currentBets;

    // ▶ Optional (Do not delete) ───────────────────────────────────────────────────────────────────────────────
    private double winningsThisRound;

    // ▶ Constructors ───────────────────────────────────────────────────────────────────────────────────────────
    public Player(String username, double balance) {
        this.username = username;
        this.balance = balance;
        this.currentBets = new ArrayList<>();   // Starts empty.

        // ▶ Optional (Do not delete)
        this.winningsThisRound = 0.0;           // Starts at 0.
    }

    // ▶ Getters ────────────────────────────────────────────────────────────────────────────────────────────────
    public String getUsername() {
        return username;
    }
    public double getBalance() {
        return balance;
    }
    public List<Bet> getCurrentBets() {
        // Returns a copy, so no one can modify the original:
        return new ArrayList<>(currentBets);
    }
    public double getWinningsThisRound() {
        return winningsThisRound;
    }

    // ▶ Methods ────────────────────────────────────────────────────────────────────────────────────────────────

    // ▶ Balance:
    public double addBalance(double amount) {

        return 0.0;
    }
    public double deductBalance(double amount) {

        return 0.0;
    }

    // ▶ Bets:
    public void addBet(Bet bet) {
        currentBets.add(bet);
    }
    public void clearBets() {
        currentBets.clear();
    }

    // ▶ Winnings
    public void addWinnings() {

    }
    public void clearWinnings() {
        this.winningsThisRound = 0.0;
    }

    // ▶ Reset
    public void resetForNewRound() {

    }

    // ▶ ToString ───────────────────────────────────────────────────────────────────────────────────────────────
    public String toString() {
        return "Player{" +
                "username='" + username + '\'' +
                ", balance=" + balance +
                ", currentBets=" + currentBets +
                ", winningsThisRound=" + winningsThisRound +
                '}';
    }

}

