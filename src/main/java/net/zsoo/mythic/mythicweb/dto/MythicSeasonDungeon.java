package net.zsoo.mythic.mythicweb.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class MythicSeasonDungeon {
    @Id
    private int season;
    @Id
    private int dungeonId;
}
