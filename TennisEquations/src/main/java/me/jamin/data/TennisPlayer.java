package me.jamin.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class TennisPlayer {

    public TennisPlayer(String name, int matches, int aces, int doubleFaults, float serveSuccessRate, float firstServeWon, float secondServeWon, float firstReturnWon, float secondReturnWon, float breakPointsSaved, float servicePointsWon, float returnPointsWon, float breakPointsConverted, float returnGamesWon, float tournamentScore) {
        this.name = name;
        this.matches = matches;
        this.aces = aces;
        this.doubleFaults = doubleFaults;
        this.serveSuccessRate = serveSuccessRate;
        this.firstServeWon = firstServeWon;
        this.secondServeWon = secondServeWon;
        this.firstReturnWon = firstReturnWon;
        this.secondReturnWon = secondReturnWon;
        this.breakPointsSaved = breakPointsSaved;
        this.servicePointsWon = servicePointsWon;
        this.returnPointsWon = returnPointsWon;
        this.breakPointsConverted = breakPointsConverted;
        this.returnGamesWon = returnGamesWon;
        this.tournamentScore = tournamentScore;
    }

    String name;
    int matches;
    int aces;
    int doubleFaults;
    // The percentage of total serves that are legal
    float serveSuccessRate;
    // Percent of successful serves won
    float firstServeWon;
    float secondServeWon;
    float firstReturnWon;
    float secondReturnWon;
    float breakPointsSaved;
    // Percent of total serves (successful or not) won
    float servicePointsWon;

    // What percent of total returns are won
    float returnPointsWon;
    float breakPointsConverted;
    float returnGamesWon;
    float tournamentScore;

    @Getter
    @Setter
    public TennisVariables variables;

    public String getAsCsv() {
        return name + "," + firstServeWon + "," + secondServeWon + "," + servicePointsWon + "," + returnPointsWon + "," + breakPointsConverted + "," + tournamentScore + "," + variables.getRankingScore();
    }

}
