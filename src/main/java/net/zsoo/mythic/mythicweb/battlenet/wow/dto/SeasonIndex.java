package net.zsoo.mythic.mythicweb.battlenet.wow.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeasonIndex {
    @JsonProperty("current_season")
    private KeyIdName currentSeason;
    private List<KeyIdName> seasons;
}
