package net.zsoo.mythic.mythicweb.battlenet.wow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Season {
    private int id;
    @JsonProperty("season_name")
    private String seasonName;
    @JsonProperty("start_timestamp")
    private long startTimestamp;
    @JsonProperty("end_timestamp")
    private long endTimestamp;
}
