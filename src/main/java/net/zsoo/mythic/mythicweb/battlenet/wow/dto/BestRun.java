package net.zsoo.mythic.mythicweb.battlenet.wow.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BestRun {
    private int duration;
    @JsonProperty("keystone_level")
    private int keystoneLevel;
    @JsonProperty("completed_timestamp")
    private long completedTimestamp;
    private List<RunMember> members;
    private KeyIdName dungeon;
    private int ranking; // leaderboard only
    @JsonProperty("mythic_rating")
    private MythicRating mythicRating;
}
