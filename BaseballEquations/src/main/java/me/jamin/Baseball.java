package me.jamin;

import me.jamin.data.BaseballPlayer;
import me.jamin.data.BaseballVariables;
import me.jamin.data.DataController;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.ArrayList;

public class Baseball {

    public Baseball() {
        DataController.INSTANCE.loadAllPlayers("Players");

        // Average hits per game
        ArrayList<Double> hitAverage = new ArrayList<>();
        // Home RUNNNS!!!
        ArrayList<Double> homerunAverage = new ArrayList<>();
        // Fielding average
        ArrayList<Double> fieldingAverage = new ArrayList<>();
        // Average strikeouts per game for pitchers
        ArrayList<Double> strikeoutAverage = new ArrayList<>();
        // Total ERA for pitchers
        ArrayList<Double> earnedRunAverage = new ArrayList<>();

        for (BaseballPlayer player : DataController.INSTANCE.getBaseballPlayers()) {
            hitAverage.add(player.getHitsAverage());
            homerunAverage.add(player.getHomeRuns());
            fieldingAverage.add(player.getTotalFieldingAverage());

            // If pitching
            if (player.getTotalStrikeouts() >= 1) {
                strikeoutAverage.add(player.getStrikeoutsAverage());
                earnedRunAverage.add(player.getTotalEarnedRunAverage());
            }
        }

        // Create distributions
        NormalDistribution hitAverageDistribution = getDistribution(hitAverage.toArray(new Double[0]));
        NormalDistribution homerunAverageDistribution = getDistribution(homerunAverage.toArray(new Double[0]));
        NormalDistribution fieldingAverageDistribution = getDistribution(fieldingAverage.toArray(new Double[0]));
        NormalDistribution strikeoutAverageDistribution = getDistribution(strikeoutAverage.toArray(new Double[0]));
        NormalDistribution earnedRunAverageDistribution = getDistribution(earnedRunAverage.toArray(new Double[0]));

        for (BaseballPlayer player : DataController.INSTANCE.getBaseballPlayers()) {
            BaseballVariables variables = new BaseballVariables();

            {
                double value = player.getHitsAverage();
                double probability = hitAverageDistribution.probability(Math.min(hitAverageDistribution.getMean(), value), Math.max(hitAverageDistribution.getMean(), value));
                double percentOfPlayers = value > hitAverageDistribution.getMean() ? 0.5 + probability : 0.5 - probability;
                variables.setHitAverage(percentOfPlayers);
            }
            {
                double value = player.getHomeRuns();
                double probability = homerunAverageDistribution.probability(Math.min(homerunAverageDistribution.getMean(), value), Math.max(homerunAverageDistribution.getMean(), value));
                double percentOfPlayers = value > homerunAverageDistribution.getMean() ? 0.5 + probability : 0.5 - probability;
                variables.setHomerunAverage(percentOfPlayers);
            }
            {
                double value = player.getTotalFieldingAverage();
                double probability = fieldingAverageDistribution.probability(Math.min(fieldingAverageDistribution.getMean(), value), Math.max(fieldingAverageDistribution.getMean(), value));
                double percentOfPlayers = value > fieldingAverageDistribution.getMean() ? 0.5 + probability : 0.5 - probability;
                variables.setFieldingAverage(percentOfPlayers);
            }
            if (player.getStrikeoutsAverage() >= 1) {
                {
                    double value = player.getStrikeoutsAverage();
                    double probability = strikeoutAverageDistribution.probability(Math.min(strikeoutAverageDistribution.getMean(), value), Math.max(strikeoutAverageDistribution.getMean(), value));
                    double percentOfPlayers = value > strikeoutAverageDistribution.getMean() ? 0.5 + probability : 0.5 - probability;
                    variables.setStrikeoutAverage(percentOfPlayers);
                }
                {
                    double value = player.getTotalEarnedRunAverage();
                    double probability = earnedRunAverageDistribution.probability(Math.min(earnedRunAverageDistribution.getMean(), value), Math.max(earnedRunAverageDistribution.getMean(), value));
                    double percentOfPlayers = value > earnedRunAverageDistribution.getMean() ? 0.5 - probability : 0.5 + probability;
                    variables.setEarnedRunAverage(percentOfPlayers);
                }
            }

            double calculation =
                    (((float) 5/9) * (((1.1 * variables.getEarnedRunAverage()) + variables.getHitAverage()) / 2.1)) +
                            (((float) 1/3) * ((variables.getFieldingAverage() + variables.getHomerunAverage() + variables.getStrikeoutAverage()) / 3.2))
                    + (((float) 1/9) * ((player.getGreatFeats() + player.getExternalContributions() + player.getLeadership()) / 3));
            variables.setRankingScore(calculation);
            player.setVariables(variables);
            System.out.println(player.getName() + " " + variables.toString() + " RANK: " + calculation);
            System.out.println(player.toString());
        }

        DataController.INSTANCE.writeToFile(DataController.INSTANCE.getBaseballPlayers(), "ranked.csv");
    }

    public static void main(String[] args) {
        new Baseball();
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
