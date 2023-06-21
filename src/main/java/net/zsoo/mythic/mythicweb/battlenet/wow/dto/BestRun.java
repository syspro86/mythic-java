package net.zsoo.mythic.mythicweb.battlenet.wow.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BestRun {
    private long duration;
    @JsonProperty("keystone_level")
    private int keystoneLevel;
    @JsonProperty("completed_timestamp")
    private long completedTimestamp;
    private List<RunMember> members;

    private KeyIdName dungeon;
    private KeyIdName season;

    // leaderboard only
    private int ranking;
}
