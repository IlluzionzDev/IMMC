package me.jamin.data;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public enum DataController {

    INSTANCE;

    @Getter
    public final ArrayList<BaseballPlayer> baseballPlayers = new ArrayList<>();

    public void loadAllPlayers(String directory) {
        // Go through directory of players
        File[] directoryListing = new File(directory).listFiles();
        // Go through each players directory
        if (directoryListing != null) {
            for (File playerDirectory : directoryListing) {
                loadBaseballPlayer(playerDirectory);
            }
        }
    }

    private void loadBaseballPlayer(File directory) {
        File[] childDir = directory.listFiles();
        // Go through each players files
        if (childDir != null) {
            BaseballPlayer player = new BaseballPlayer();
            for (File dataFile : childDir) {
                player.setName(directory.getName());
                // Based on each stat
                String name = dataFile.getName().split("\\.")[0].toLowerCase();
                if (name.equalsIgnoreCase("batting")) {
                    player = loadBattingData(player, dataFile);
                } else if (name.equalsIgnoreCase("fielding")) {
                    player = loadFieldingData(player, dataFile);
                } else if (name.equalsIgnoreCase("pitching")) {
                    player = loadPitchingData(player, dataFile);
                }
            }

            // Load external contributions
            player = loadContributions(player, "contributions.csv");

            baseballPlayers.add(player);
        }
    }

    private BaseballPlayer loadContributions(BaseballPlayer player, String dataFile) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(dataFile))) {
            String line = reader.readLine();

            while (line != null) {
                String[] attributes = line.split(",");

                try {
                    // Make sure matches name then load stats
                    if (attributes[0].equalsIgnoreCase(player.getName().replaceAll(",", ""))) {
                        player.setExternalContributions(Double.parseDouble(attributes[1]) / 4);
                        player.setLeadership(Double.parseDouble(attributes[2]) / 4);
                        player.setGreatFeats(Double.parseDouble(attributes[3]) / 4);
                    }
                } catch (final Throwable ignored) {
                    line = reader.readLine();
                    continue;
                }

                // Read next
                line = reader.readLine();
            }

        } catch (final Throwable ex) {
            ex.printStackTrace();
        }

        return player;
    }

    private BaseballPlayer loadBattingData(BaseballPlayer player, File dataFile) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(dataFile.getPath()))) {
            String line = reader.readLine();

            // Load total stats
            int totalGames = 0;
            int totalHits = 0;
            int homeRuns = 0;
            int yearsActive = 0;
            while (line != null) {
                String[] attributes = line.split(",");

                try {
                    totalGames += Integer.parseInt(attributes[4]);
                    totalHits += Integer.parseInt(attributes[8]);
                    homeRuns += Integer.parseInt(attributes[11]);
                    yearsActive++;
                } catch (final Throwable ignored) {
                    line = reader.readLine();
                    continue;
                }

                // Read next
                line = reader.readLine();
            }

            player.setTotalGames(totalGames);
            player.setTotalHits(totalHits);
            player.setHomeRuns(homeRuns);
            player.setYearsActive(yearsActive);
        } catch (final Throwable ex) {
            ex.printStackTrace();
        }

        return player;
    }

    private BaseballPlayer loadFieldingData(BaseballPlayer player, File dataFile) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(dataFile.getPath()))) {
            String line = reader.readLine();

            // Load total stats
            double totalFieldingAverage = 0;
            int entries = 0;
            while (line != null) {
                String[] attributes = line.split(",");

                try {
                    totalFieldingAverage += Double.parseDouble(attributes[14]);
                    entries++;
                } catch (final Throwable ignored) {
                    line = reader.readLine();
                    continue;
                }

                // Read next
                line = reader.readLine();
            }

            player.setTotalFieldingAverage(totalFieldingAverage / entries);
        } catch (final Throwable ex) {
            ex.printStackTrace();
        }

        return player;
    }

    private BaseballPlayer loadPitchingData(BaseballPlayer player, File dataFile) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(dataFile.getPath()))) {
            String line = reader.readLine();

            // Load total stats
            int totalStrikeouts = 0;
            double totalEarnedRunSum = 0;
            int entries = 0;
            while (line != null) {
                String[] attributes = line.split(",");

                try {
                    totalStrikeouts += Integer.parseInt(attributes[21]);
                    totalEarnedRunSum += Double.parseDouble(attributes[7]);
                    entries++;
                } catch (final Throwable ignored) {
                    line = reader.readLine();
                    continue;
                }

                // Read next
                line = reader.readLine();
            }

            player.setTotalStrikeouts(totalStrikeouts);
            player.setTotalEarnedRunAverage(totalEarnedRunSum / entries);
        } catch (final Throwable ex) {
            ex.printStackTrace();
        }

        return player;
    }

    public void writeToFile(ArrayList<BaseballPlayer> objects, String path) {
        File csvOutputFile = new File(path);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            pw.println("Name,Hits Average,Homerun Average,Fielding Average,Strikeouts Average,Earned Run Average,Score");
            objects.stream()
                    .map(BaseballPlayer::getAsCsv)
                    .forEach(pw::println);
        } catch (final Throwable ex) {
            ex.printStackTrace();
        }
    }

}
