package model;

public class Game {

    private int gameId;
    private String gameName;
    private double pricePerHour;

    public Game(int gameId, String gameName, double pricePerHour) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.pricePerHour = pricePerHour;
    }

    public int getGameId() { return gameId; }
    public String getGameName() { return gameName; }
    public double getPricePerHour() { return pricePerHour; }
}