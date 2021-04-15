package me.jamin;

import lombok.Getter;
import me.jamin.data.TennisPlayer;

import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public enum DataController {

    INSTANCE;

    @Getter
    public ArrayList<TennisPlayer> tennisPlayersPopulaton = new ArrayList<>();
    @Getter
    public ArrayList<TennisPlayer> tennisPlayersSample = new ArrayList<>();

    void loadTennisPlayersSample(String fileName) {
        this.tennisPlayersSample = loadTennisData(fileName);
    }

    void loadTennisPlayersPopulation(String fileName) {
        this.tennisPlayersPopulaton = loadTennisData(fileName);
    }

    ArrayList<TennisPlayer> loadTennisData(String fileName) {
        ArrayList<TennisPlayer> data = new ArrayList<>();
        Path pathToFile = Paths.get(fileName); // create an instance of BufferedReader // using try with resource, Java 7 feature to close resources
        try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.UTF_8)) {
            String line = br.readLine();
            boolean skip = true;
            while (line != null) {
                if (skip) {
                    skip = false;
                    continue;
                }
                // use string.split to load a string array with the values from // each line of // the file, using a comma as the delimiter
                String[] attributes = line.split(",");

                String name = attributes[0];
                int matchesPlayed = Integer.parseInt(attributes[1]);
                int aces = Integer.parseInt(attributes[2]);
                int doubleFaults = Integer.parseInt(attributes[3]);
                float firstServeWon = Float.parseFloat(attributes[4]);
                float serveSuccessRate = Float.parseFloat(attributes[5]);
                float secondServeWon = Float.parseFloat(attributes[6]);
                float servicePointsWon = Float.parseFloat(attributes[7]);
                float breakPointsSaved = Float.parseFloat(attributes[8]);
                float firstReturnWon = Float.parseFloat(attributes[9]);
                float secondReturnWon = Float.parseFloat(attributes[10]);
                float returnGamesWon = Float.parseFloat(attributes[11]);
                float breakPointsConverted = Float.parseFloat(attributes[12]);
                float returnPointsWon = Float.parseFloat(attributes[13]);
                float tournamentScore = attributes.length < 15 ? 0 : Float.parseFloat(attributes[14]);

                // Build object
                data.add(new TennisPlayer(
                        name,
                        matchesPlayed,
                        aces,
                        doubleFaults,
                        serveSuccessRate,
                        firstServeWon,
                        secondServeWon,
                        firstReturnWon,
                        secondReturnWon,
                        breakPointsSaved,
                        servicePointsWon,
                        returnPointsWon,
                        breakPointsConverted,
                        returnGamesWon,
                        tournamentScore
                ));

                line = br.readLine();
            }
        } catch (final Throwable t) {
            t.printStackTrace();
        }

        return data;
    }

    public void writeToFile(ArrayList<TennisPlayer> objects, String path) {
        File csvOutputFile = new File(path);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            objects.stream()
                    .map(TennisPlayer::getAsCsv)
                    .forEach(pw::println);
        } catch (final Throwable ex) {
            ex.printStackTrace();
        }
    }

}
