package models;

import models.bets.Bet;
import exceptions.InsufficientBalanceException;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the Rainbow Roulette game.
 * Manages balance and current bets.
 */
public class Player {
    // ▶ Attributes ─────────────────────────────────────────────────────────────────────────────────────────────
    private final String username;
    private double balance;         // Player's money balance
    private List<Bet> currentBets;

    // ▶ Optional (Do not delete) ───────────────────────────────────────────────────────────────────────────────
    private double winningsThisRound; // Accumulated winnings from the current spin

    // ▶ Constructors ───────────────────────────────────────────────────────────────────────────────────────────
    public Player(String username, double balance) {
        this.username = username;
        this.balance = balance;
        this.currentBets = new ArrayList<>();
        this.winningsThisRound = 0.0;
    }

    // ▶ Getters ────────────────────────────────────────────────────────────────────────────────────────────────
    public String getUsername() { return username; }
    public double getBalance() { return balance; }
    public List<Bet> getCurrentBets() {
        return new ArrayList<>(currentBets);
    }
    public double getWinningsThisRound() { return winningsThisRound; }

    // ▶ Methods ────────────────────────────────────────────────────────────────────────────────────────────────

    /** Adds the specified amount to the player's balance. */
    public double addBalance(double amount) {
        this.balance += amount;
        return this.balance;
    }

    /** Deducts the specified amount from the balance, checking for sufficiency. */
    public double deductBalance(double amount) {
        if (amount > this.balance) {
            throw new InsufficientBalanceException("Insufficient balance.");
        }
        this.balance -= amount;
        return this.balance;
    }

    /** Adds a bet to the player's current list of wagers. */
    public void addBet(Bet bet) {
        currentBets.add(bet);
    }

    /** Clears all current bets after a round. */
    public void clearBets() {
        currentBets.clear();
    }

    /** Accumulates net winnings from a single bet. */
    public void addWinnings(double amount) {
        this.winningsThisRound += amount;
    }

    /** Settles the total winnings of the round into the main balance. */
    public void addWinnings() {
        this.balance += this.winningsThisRound;
        clearWinnings();
    }

    /** Resets the round's accumulated winnings to zero. */
    public void clearWinnings() {
        this.winningsThisRound = 0.0;
    }

    /** Prepares the player for a new betting round. */
    public void resetForNewRound() {
        clearBets();
        clearWinnings();
    }

    // ▶ ToString ───────────────────────────────────────────────────────────────────────────────────────────────
    @Override
    public String toString() {
        return "Player{" +
                "username='" + username + '\'' +
                ", balance=" + balance +
                ", currentBets=" + currentBets +
                ", winningsThisRound=" + winningsThisRound +
                '}';
    }
}