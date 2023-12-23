package net.zsoo.mythic.mythicweb.battlenet.wow.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MythicLeaderboardPeriod {
    private KeyIdName map;
    private int period;
    @JsonProperty("period_start_timestamp")
    private long periodStartTimestamp;
    @JsonProperty("period_end_timestamp")
    private long periodEndTimestamp;
    @JsonProperty("leading_groups")
    private List<BestRun> leadingGroups;
    @JsonProperty("map_challenge_mode_id")
    private int mapChallengeModeId;
    @JsonProperty("keystone_affixes")
    private List<KeystoneAffix> keystoneAffixes;
    private String name;
}
