package net.zsoo.mythic.mythicweb.battlenet.wow.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MythicKeystoneProfileSeason {
    private KeyIdName season;
    @JsonProperty("best_runs")
    private List<BestRun> bestRuns;
}
