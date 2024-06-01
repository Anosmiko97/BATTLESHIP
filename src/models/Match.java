package models;

public class Match {
    private int id;
    private String userId;
    private boolean victory;
    private int sunkenBoats;
    private int score;
    private int numberOfShots;
    private String opponentName; 

    public Match(int id, String userId, boolean victory, int sunkenBoats, int score, int numberOfShots, String opponentName) {
        this.id = id;
        this.userId = userId;
        this.victory = victory;
        this.sunkenBoats = sunkenBoats;
        this.score = score;
        this.numberOfShots = numberOfShots;
        this.opponentName = opponentName; 
    }

    /* Getters y setters */
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }
    public void setUser(String userId) {
        this.userId = userId;
    }

    public boolean getVictory() {
        return victory;
    }
    public void setVictory(boolean victory) {
        this.victory = victory;
    }

    public int getSunkenBoats() {
        return sunkenBoats;
    }
    public void setSunkenBoats(int sunkenBoats) {
        this.sunkenBoats = sunkenBoats;
    }

    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }

    public int getNumberOfShots() {
        return numberOfShots;
    }
    public void setNumberOfShots(int numberOfShots) {
        this.numberOfShots = numberOfShots;
    }

    public String getOpponentName() {
        return opponentName;
    }
    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }


}
