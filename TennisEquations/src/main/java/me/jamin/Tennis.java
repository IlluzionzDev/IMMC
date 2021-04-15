package me.jamin;

import me.jamin.data.TennisPlayer;
import me.jamin.data.TennisVariables;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.ArrayList;

public class Tennis {

    public Tennis() {
        DataController.INSTANCE.loadTennisPlayersPopulation("tennis.csv");
        DataController.INSTANCE.loadTennisPlayersSample("tennis_grand_slam.csv");

        ArrayList<Double> diminishingDifferenceValues = new ArrayList<>();
        ArrayList<Double> breakpointConversionValues = new ArrayList<>();
        ArrayList<Double> serveSuccessRateValues = new ArrayList<>();
        ArrayList<Double> returnSuccessRateValues = new ArrayList<>();
        ArrayList<Double> tournamentScoreValues = new ArrayList<>();

        // Calculate base variables
        for (TennisPlayer player : DataController.INSTANCE.getTennisPlayersPopulaton()) {
            // Values to calculate mean and SD
            double diminishingDifference = player.getFirstServeWon() - player.getSecondServeWon();
            double breakpointConversion = player.getBreakPointsConverted();
            double serveSuccessRate = player.getServicePointsWon();
            double returnSuccessRate = player.getReturnPointsWon();
            diminishingDifferenceValues.add(diminishingDifference);
            breakpointConversionValues.add(breakpointConversion);
            serveSuccessRateValues.add(serveSuccessRate);
            returnSuccessRateValues.add(returnSuccessRate);
        }

        DataController.INSTANCE.getTennisPlayersSample().forEach(player -> {
            double tournamentScore = player.getTournamentScore();
            tournamentScoreValues.add(tournamentScore);
        });

        // Create distributions
        NormalDistribution diminishingDistribution = getDistribution(diminishingDifferenceValues.toArray(new Double[0]));
        NormalDistribution breakpointConversionDistribution = getDistribution(breakpointConversionValues.toArray(new Double[0]));
        NormalDistribution serveSuccessRateDistribution = getDistribution(serveSuccessRateValues.toArray(new Double[0]));
        NormalDistribution returnSuccessRateDistribution = getDistribution(returnSuccessRateValues.toArray(new Double[0]));
        NormalDistribution tournamentScoreDistribution = getDistribution(tournamentScoreValues.toArray(new Double[0]));
        for (TennisPlayer player : DataController.INSTANCE.getTennisPlayersSample()) {
            // Variables
            TennisVariables variables = new TennisVariables();

            {
                double value = player.getFirstServeWon() - player.getSecondServeWon();
                double probability = diminishingDistribution.probability(Math.min(diminishingDistribution.getMean(), value), Math.max(diminishingDistribution.getMean(), value));
                double percentOfPlayers = value > diminishingDistribution.getMean() ? 0.5 - probability : 0.5 + probability;
                variables.setDiminishingPercentage(percentOfPlayers);
            }
            {
                double value = player.getBreakPointsConverted();
                double probability = breakpointConversionDistribution.probability(Math.min(breakpointConversionDistribution.getMean(), value), Math.max(breakpointConversionDistribution.getMean(), value));
                double percentOfPlayers = value > breakpointConversionDistribution.getMean() ? 0.5 + probability : 0.5 - probability;
                variables.setBreakPointConversion(percentOfPlayers);
            }
            {
                double value = player.getServicePointsWon();
                double probability = serveSuccessRateDistribution.probability(Math.min(serveSuccessRateDistribution.getMean(), value), Math.max(serveSuccessRateDistribution.getMean(), value));
                double percentOfPlayers = value > serveSuccessRateDistribution.getMean() ? 0.5 + probability : 0.5 - probability;
                variables.setServeSuccessRate(percentOfPlayers);
            }
            {
                double value = player.getReturnPointsWon();
                double probability = returnSuccessRateDistribution.probability(Math.min(returnSuccessRateDistribution.getMean(), value), Math.max(returnSuccessRateDistribution.getMean(), value));
                double percentOfPlayers = value > returnSuccessRateDistribution.getMean() ? 0.5 + probability : 0.5 - probability;
                variables.setReturnSuccessRate(percentOfPlayers);
            }
            {
                double value = player.getTournamentScore();
                double probability = tournamentScoreDistribution.probability(Math.min(tournamentScoreDistribution.getMean(), value), Math.max(tournamentScoreDistribution.getMean(), value));
                double percentOfPlayers = value > tournamentScoreDistribution.getMean() ? 0.5 + probability : 0.5 - probability;
                variables.setTournamentScore(percentOfPlayers);
            }

            double calculation =
                    (0.5 * (((1.2 * variables.getBreakPointConversion()) + variables.getDiminishingPercentage()) / 2.2)) +
                    (0.3 * ((variables.getServeSuccessRate() + variables.getReturnSuccessRate()) / 2)) +
                    (0.2 * variables.getTournamentScore());
            variables.setRankingScore(calculation);

            player.setVariables(variables);
            System.out.println(player.getName() + " " + variables + " " + calculation);
        }

        DataController.INSTANCE.writeToFile(DataController.INSTANCE.getTennisPlayersSample(), "ranked.csv");
    }

    public static void main(String[] args) {
        new Tennis();
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
