package me.jamin.data;

import lombok.*;

/**
 * Total stats over whole career
 */
@Data
@ToString
@NoArgsConstructor
public class BaseballPlayer {

    String name;

    // Batting
    @NonNull
    int totalHits;
    double homeRuns;

    // Fielding
    @NonNull
    double totalFieldingAverage;

    // Pitching
    int totalStrikeouts;
    double totalEarnedRunAverage;

    int totalGames;
    int yearsActive;

    // Contributions
    double externalContributions;
    double leadership;
    double greatFeats;

    BaseballVariables variables;

    public double getHitsAverage() {
        return (double) totalHits / totalGames;
    }

    public double getStrikeoutsAverage() {
        return (double) totalStrikeouts / totalGames;
    }

    public String getAsCsv() {
        return name.replaceAll(",", "") + "," + getHitsAverage() + "," + homeRuns + "," + totalFieldingAverage + "," + getStrikeoutsAverage() + "," + totalEarnedRunAverage + "," + variables.getRankingScore();
    }

}
