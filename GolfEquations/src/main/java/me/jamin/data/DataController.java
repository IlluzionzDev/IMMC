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
    public ArrayList<GolfPlayer> players = new ArrayList<>();

    public void loadData(String fileName) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(fileName))) {
            String line = reader.readLine();

            // Load total stats
            while (line != null) {
                String[] attributes = line.split(",");

                try {
                    GolfPlayer player = new GolfPlayer();

                    player.setName(attributes[0]);
                    player.setDriveDistance(Double.parseDouble(attributes[1]));
                    player.setDriveAccuracy(Double.parseDouble(attributes[2]));
                    player.setPuttsAverage(Double.parseDouble(attributes[3]));
                    player.setGreatFeats(Double.parseDouble(attributes[4]) / 4);
                    player.setExternalContributions(Double.parseDouble(attributes[5]) / 4);
                    players.add(player);
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
    }

    public void writeToFile(ArrayList<GolfPlayer> objects, String path) {
        File csvOutputFile = new File(path);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            pw.println("Name,Drive Distance,Drive Accuracy,Putts Average,Score");
            objects.stream()
                    .map(GolfPlayer::getAsCsv)
                    .forEach(pw::println);
        } catch (final Throwable ex) {
            ex.printStackTrace();
        }
    }

}
