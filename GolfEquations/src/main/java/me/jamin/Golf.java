package me.jamin;

import me.jamin.data.DataController;
import me.jamin.data.GolfPlayer;
import me.jamin.data.GolfVariables;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.ArrayList;

public class Golf {

    public Golf() {
        DataController.INSTANCE.loadData("data.csv");

        ArrayList<Double> driveDistanceAverage = new ArrayList<>();
        ArrayList<Double> driveAccuracyAverage = new ArrayList<>();
        ArrayList<Double> puttsAverage = new ArrayList<>();

        for (GolfPlayer player : DataController.INSTANCE.getPlayers()) {
            driveDistanceAverage.add(player.getDriveDistance());
            driveAccuracyAverage.add(player.getDriveAccuracy());
            puttsAverage.add(player.getPuttsAverage());
        }

        // Create distributions
        NormalDistribution driveDistanceAverageDistribution = getDistribution(driveDistanceAverage.toArray(new Double[0]));
        NormalDistribution driveAccuracyAverageDistribution = getDistribution(driveAccuracyAverage.toArray(new Double[0]));
        NormalDistribution puttsAverageDistribution = getDistribution(puttsAverage.toArray(new Double[0]));

        for (GolfPlayer player : DataController.INSTANCE.getPlayers()) {
            GolfVariables variables = new GolfVariables();

            {
                double value = player.getDriveDistance();
                double probability = driveDistanceAverageDistribution.probability(Math.min(driveDistanceAverageDistribution.getMean(), value), Math.max(driveDistanceAverageDistribution.getMean(), value));
                double percentOfPlayers = value > driveDistanceAverageDistribution.getMean() ? 0.5 + probability : 0.5 - probability;
                variables.setDriveDistance(percentOfPlayers);
            }
            {
                double value = player.getDriveAccuracy();
                double probability = driveAccuracyAverageDistribution.probability(Math.min(driveAccuracyAverageDistribution.getMean(), value), Math.max(driveAccuracyAverageDistribution.getMean(), value));
                double percentOfPlayers = value > driveAccuracyAverageDistribution.getMean() ? 0.5 + probability : 0.5 - probability;
                variables.setDriveAccuracy(percentOfPlayers);
            }
            {
                double value = player.getPuttsAverage();
                double probability = puttsAverageDistribution.probability(Math.min(puttsAverageDistribution.getMean(), value), Math.max(puttsAverageDistribution.getMean(), value));
                double percentOfPlayers = value > puttsAverageDistribution.getMean() ? 0.5 - probability : 0.5 + probability;
                variables.setPuttsAverage(percentOfPlayers);
            }

            double calculation =
                    (0.3 * variables.getPuttsAverage()) +
                            (0.5 * ((variables.getDriveDistance() + (1.2 * variables.getDriveAccuracy())) / 2.2))
                            + (0.2 * ((player.getGreatFeats() + player.getExternalContributions()) / 2));
            variables.setRankingScore(calculation);
            player.setVariables(variables);
            System.out.println(player.getName() + " " + variables.toString() + " RANK: " + calculation);
        }

        DataController.INSTANCE.writeToFile(DataController.INSTANCE.getPlayers(), "ranked.csv");
    }

    public static void main(String[] args) {
        new Golf();
    }

    public static NormalDistribution getDistribution(Double[] stats) {
        double sd = calculateSD(stats);
        double sum = 0;
        for (int i = 0; i < stats.length; i++) {
            sum += stats[i];
        }
        double mean =  sum / stats.length;
        return new NormalDistribution(mean, sd);
    }

    public static double calculateSD(Double[] numArray) {
        double sum = 0.0, standardDeviation = 0.0;
        int length = numArray.length;
        for(double num : numArray) {
            sum += num;
        }
        double mean = sum/length;
        for (double num: numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }
        return Math.sqrt(standardDeviation/length);
    }

}
