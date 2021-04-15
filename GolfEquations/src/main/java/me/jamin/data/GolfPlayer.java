package me.jamin.data;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class GolfPlayer {

    String name;

    double driveDistance;
    double driveAccuracy;
    double puttsAverage;
    double greatFeats;
    double externalContributions;

    GolfVariables variables;

    public String getAsCsv() {
        return name + "," + driveDistance + "," + driveAccuracy + "," + puttsAverage + "," + variables.getRankingScore();
    }

}
