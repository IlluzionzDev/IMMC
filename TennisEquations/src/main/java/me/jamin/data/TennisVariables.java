package me.jamin.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@ToString
@Data
public class TennisVariables {

    double diminishingPercentage;
    double breakPointConversion;
    double serveSuccessRate;
    double returnSuccessRate;
    double tournamentScore;

    double rankingScore;

}
