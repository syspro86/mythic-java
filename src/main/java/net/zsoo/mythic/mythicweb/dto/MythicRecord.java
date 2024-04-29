package net.zsoo.mythic.mythicweb.dto;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class MythicRecord {

    @Id
    private String recordId;
    private int season;
    private int period;
    private int dungeonId;
    private int duration;
    private long completedTimestamp;
    private int keystoneLevel;
    private int keystoneUpgrade;
    private float mythicRating;

    @OneToMany(mappedBy = "record", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    List<MythicRecordPlayer> players;
}
