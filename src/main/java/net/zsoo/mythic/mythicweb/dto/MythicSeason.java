package net.zsoo.mythic.mythicweb.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class MythicSeason {

    @Id
    private int season;
    private String seasonName;
    private long startTimestamp;
    private Long endTimestamp;
}
